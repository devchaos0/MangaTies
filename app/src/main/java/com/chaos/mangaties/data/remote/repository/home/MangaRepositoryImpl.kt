package com.chaos.mangaties.data.remote.repository.home

import android.util.Log
import com.chaos.mangaties.data.remote.MangaDexApi
import com.chaos.mangaties.domain.model.manga.home.MangaHome
import com.chaos.mangaties.domain.model.manga.manga.ChapterPages
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import com.chaos.mangaties.domain.model.manga.manga.MangaDetails
import com.chaos.mangaties.domain.repository.manga.MangaRepository

class MangaRepositoryImpl(
    private val api: MangaDexApi
) : MangaRepository {

    private val TAG = "MangaRepository"


    override suspend fun getMangaList(offset: Int, limit: Int): Result<List<MangaHome>> {
        return try {
            val response = api.getMangaList(
                limit = limit,
                offset = offset,
                availableTranslatedLanguages = listOf("en")
            )

            val mangaList = response.data.mapNotNull { mangaDto ->
                val title = mangaDto.attributes?.title?.get("en")
                    ?: mangaDto.attributes?.title?.values?.firstOrNull()
                    ?: "Unknown Title"

                val coverFileName = mangaDto.relationships
                    .find { it.type == "cover_art" }
                    ?.attributes
                    ?.fileName

                MangaHome(
                    id = mangaDto.id,
                    title = title,
                    coverFileName = coverFileName
                )
            }

            Result.success(mangaList)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching manga list", e)
            Result.failure(e)
        }
    }

    override suspend fun getMangaDetails(mangaId: String): Result<MangaDetails> {
        return try {
            val response = api.getMangaDetails(mangaId)
            val data = response.data

            val title = data.attributes.title["en"]
                ?: data.attributes.title.values.firstOrNull()
                ?: "Uknown title"

            val description = data.attributes.description?.get("en")
                ?: data.attributes.description?.values?.firstOrNull()
                ?: "No description available"

            val coverFile = data.relationships
                .find { it.type == "cover_art" }
                ?.attributes
                ?.fileName

            val mangaDetails = MangaDetails(
                id = data.id,
                title = title,
                description = description,
                status = data.attributes.status,
                contentRating = data.attributes.contentRating,
                tags = data.attributes.tags,
                coverFileName = coverFile,
                year = data.attributes.year,
                originalLanguage = data.attributes.originalLanguage,
                publicationDemographic = data.attributes.publicationDemographic
            )

            Result.success(mangaDetails)
        } catch (e: Exception){
            Log.e(TAG, "Error fetching manga detail for ID: $mangaId", e)
            Result.failure(e)
        }
    }

    override suspend fun getMangaChapters(mangaId: String, offset: Int, limit: Int): Result<List<MangaChapter>> {
        return try {
            val response = api.getMangaChapters(mangaId, offset = offset, limit = limit)
            val chapters = response.data.mapNotNull { chapterDto ->
                val chapterNumber = chapterDto.attributes.chapter

                if (chapterNumber.isNotEmpty()){
                    MangaChapter(
                        id = chapterDto.id,
                        chapter = chapterNumber,
                        title = chapterDto.attributes.title,
                        volume = chapterDto.attributes.volume ?: "?",
                        pages = chapterDto.attributes.pages,
                        updatedAt = chapterDto.attributes.updatedAt
                    )
                } else {
                    null
                }
            }

            Result.success(chapters)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching chapters for manga ID: $mangaId", e)
            Result.failure(e)
        }
    }

    override suspend fun getChapterPages(chapterId: String): Result<ChapterPages> {
        return try {
            val response = api
                .getChapterPages(chapterId)

            val chapterPages = ChapterPages(
                chapterId = chapterId,
                chapterNumber = "",
                baseUrl = response.baseUrl,
                hash = response.chapter.hash,
                data = response.chapter.data,
                dataSaver = response.chapter.dataSaver,
            )

            Result.success(chapterPages)
        } catch (e: Exception){
            Log.e(TAG, "Error fetching chapter pages for ID: $chapterId", e)
            Result.failure(e)
        }
    }
}