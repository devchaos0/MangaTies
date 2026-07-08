package com.chaos.mangaties.domain.repository.manga

import com.chaos.mangaties.domain.model.manga.manga.DownloadedChapter
import com.chaos.mangaties.domain.model.manga.manga.DownloadedManga
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun downloadChapter(
        mangaId: String,
        mangaTitle: String,
        chapterId: String,
        chapterNumber: String,
        title: String?,
        volume: String?,
        pageUrls: List<String>,
        onProgress: (Float) -> Unit
    ) : Result<Unit>

    suspend fun getDownloadedManga(): Flow<List<DownloadedManga>>
    suspend fun getDownloadedChapters(mangaId: String) : Flow<List<DownloadedChapter>>
    suspend fun deleteDownloadedChapter(chapterId: String) : Result<Unit>
    suspend fun deleteDownloadedManga(mangaId: String) : Result<Unit>
    suspend fun isChapterDownloaded(chapterId: String) : Boolean
    suspend fun refreshDownloadedMangas()

    suspend fun deleteAllDownloadedMangas(): Result<Unit>
}