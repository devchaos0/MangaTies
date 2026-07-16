package com.chaos.mangaties.data.remote.repository

import android.content.Context
import android.util.Log
import com.chaos.mangaties.domain.model.manga.manga.DownloadedChapter
import com.chaos.mangaties.domain.model.manga.manga.DownloadedManga
import com.chaos.mangaties.domain.repository.manga.DownloadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class ChapterMetadata(
    val chapterId: String,
    val chapterNumber: String,
    val title: String? = null,
    val volume: String? = null,
    val downloadedAt: Long
)

@Serializable
data class MangaMetadata(
    val mangaId: String,
    val mangaTitle: String,
    val coverFileName: String? = null
)

@Singleton
class DownloadRepositoryImpl @Inject constructor(
    private val context: Context
) : DownloadRepository{
    private val TAG = "DownloadRepository"
    private val json = Json { ignoreUnknownKeys = true }
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .dispatcher(Dispatcher().apply {
            maxRequests = 20
            maxRequestsPerHost = 10
        })
        .build()

    private val _downloadedMangas = MutableStateFlow<List<DownloadedManga>>(emptyList())

    private val downloadDir: File by lazy {
        File(context.filesDir, "downloads").apply {
            if (!exists()) mkdirs()
        }
    }

    init {
        loadDownloadedMangas()
    }

    private fun loadDownloadedMangas() {
        try {
            val mangas = mutableListOf<DownloadedManga>()
            downloadDir.listFiles()?.forEach { mangaDir ->

                if (mangaDir.isDirectory){
                    val chapters = mutableListOf<DownloadedChapter>()
                    var mangaTitle = mangaDir.name.split("_").getOrElse(1) { mangaDir.name }
                    var coverFileName: String? = null

                    val mangaMetadataFile = File(mangaDir, "manga_metadata.json")
                    if (mangaMetadataFile.exists()){
                        try {
                            val mangaMetadata = json.decodeFromString<MangaMetadata>(
                                mangaMetadataFile.readText()
                            )
                            mangaTitle = mangaMetadata.mangaTitle
                            coverFileName = mangaMetadata.coverFileName
                        } catch (e: Exception){
                            Log.e(TAG, "Error reading manga metadata", e)
                        }
                    }

                    mangaDir.listFiles()?.forEach { chapterDir ->
                        if (chapterDir.isDirectory){
                            val metadataFIle = File(chapterDir, "metadata.json")
                            if (metadataFIle.exists()){
                                try {
                                    val metadata = json.decodeFromString<ChapterMetadata>(
                                        metadataFIle.readText()
                                    )

                                    val pages = chapterDir.listFiles()
                                        ?.filter { it.name != "metadata.json" }
                                        ?.sortedBy { it.name }
                                        ?.map { it.absolutePath }
                                        ?: emptyList()

                                    if (pages.isNotEmpty()){
                                        chapters.add(
                                            DownloadedChapter(
                                                chapterId = metadata.chapterId,
                                                chapterNumber = metadata.chapterNumber,
                                                title = metadata.title,
                                                volume = metadata.volume,
                                                pages = pages,
                                                downloadedAt = metadata.downloadedAt
                                            )
                                        )
                                    }
                                } catch (e: Exception){
                                    Log.e(TAG, "Error reading metadata for ${chapterDir.name}", e)
                                }
                            }
                        }
                    }

                    if (chapters.isNotEmpty()){
                        mangas.add(
                            DownloadedManga(
                                mangaId = mangaDir.name.split("_").firstOrNull() ?: mangaDir.name,
                                mangaTitle = mangaTitle,
                                coverFileName = coverFileName,
                                downLoadedChapters = chapters.sortedBy { it.chapterNumber.toDoubleOrNull() ?: 0.0 }
                            )
                        )
                    }
                }
                _downloadedMangas.value = mangas
            }
        } catch (e: Exception){
            Log.e(TAG, "Error loading downloaded mangas", e)
        }
    }

    override suspend fun downloadChapter(
        mangaId: String,
        mangaTitle: String,
        chapterId: String,
        chapterNumber: String,
        title: String?,
        volume: String?,
        pageUrls: List<String>,
        onProgress: (Float) -> Unit
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val mangaDir = File(downloadDir,  "${mangaId}_${mangaTitle.replace(" ", "_")}")

            if (!mangaDir.exists()) {
                mangaDir.mkdirs()
            }

            // Save manga metadata
            val mangaMetadataFile = File(mangaDir, "manga_metadata.json")
            if (!mangaMetadataFile.exists()) {
                val metadata = MangaMetadata(
                    mangaId = mangaId,
                    mangaTitle = mangaTitle,
                    coverFileName = null 
                )
                mangaMetadataFile.writeText(json.encodeToString(metadata))
            }

            val chapterDir = File(mangaDir, chapterId)
            if (!chapterDir.exists()) chapterDir.mkdirs()

            coroutineScope {
                pageUrls.mapIndexed { index, imageUrl ->
                    async {
                        val fileName = "${index.toString().padStart(4, '0')}_${imageUrl.substringAfterLast("/")}"
                        val file = File(chapterDir, fileName)

                        if (!file.exists()) {
                            val request = Request.Builder()
                                .url(imageUrl)
                                .build()

                            client.newCall(request).execute().use { response ->
                                if (response.isSuccessful) {
                                    response.body?.byteStream()?.use { inputStream ->
                                        FileOutputStream(file).use { outputStream ->
                                            inputStream.copyTo(outputStream)
                                        }
                                    }
                                } else {
                                    throw Exception("Failed to download page: ${response.code}")
                                }
                            }
                        }
                    }
                }.awaitAll()
            }

            val metadata = ChapterMetadata(
                chapterId = chapterId,
                chapterNumber = chapterNumber,
                title = title,
                volume = volume,
                downloadedAt = System.currentTimeMillis()
            )

            val metadataFile = File(chapterDir, "metadata.json")
            metadataFile.writeText(json.encodeToString(metadata))

            refreshDownloadedMangas()

            Result.success(Unit)
        } catch (e: Exception){
            Log.e(TAG, "Error downloading chapter", e)
            Result.failure(e)
        }
    }

    override suspend fun getDownloadedManga(): Flow<List<DownloadedManga>> {
        return _downloadedMangas.asStateFlow()
    }

    override suspend fun getDownloadedChapters(mangaId: String): Flow<List<DownloadedChapter>> {
        val manga = _downloadedMangas.value.find { it.mangaId == mangaId }
        return MutableStateFlow(manga?.downLoadedChapters ?: emptyList()).asStateFlow()
    }

    override suspend fun deleteDownloadedChapter(chapterId: String): Result<Unit> {
        return try {
            val chapterDir = downloadDir.listFiles()?.find { mangaDir ->
                mangaDir.listFiles()?.any { it.name == chapterId } ?: false
            }?.let { mangaDir ->
                File(mangaDir,chapterId)
            }

            chapterDir?.deleteRecursively()
            refreshDownloadedMangas()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteDownloadedManga(mangaId: String): Result<Unit> {
        return try {

            val mangaDir = downloadDir.listFiles()?.find { it.name.startsWith(mangaId) }

            mangaDir?.deleteRecursively()
            refreshDownloadedMangas()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteAllDownloadedMangas(): Result<Unit> {
        return try {
            downloadDir.listFiles()?.forEach { file ->
                if (file.isDirectory){
                    file.deleteRecursively()
                }
            }
            refreshDownloadedMangas()
            Result.success(Unit)
        } catch (e: Exception){
            Log.e(TAG, "Error deleting all mangas", e)
            Result.failure(e)
        }
    }

    override suspend fun isChapterDownloaded(chapterId: String): Boolean {
        return _downloadedMangas.value.any{ manga ->
            manga.downLoadedChapters.any { it.chapterId == chapterId }
        }
    }

    override suspend fun refreshDownloadedMangas() {
        loadDownloadedMangas()
    }

}