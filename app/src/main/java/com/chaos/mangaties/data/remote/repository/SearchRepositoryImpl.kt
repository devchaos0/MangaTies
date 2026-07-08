package com.chaos.mangaties.data.remote.repository

import android.util.Log
import com.chaos.mangaties.data.remote.MangaDexApi
import com.chaos.mangaties.domain.model.manga.manga.SearchManga
import com.chaos.mangaties.domain.repository.manga.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val api: MangaDexApi
) : SearchRepository{

    private val TAG = "SearchRepository"

    override suspend fun searchManga(
        query: String,
        offset: Int,
        limit: Int
    ): Result<Pair<List<SearchManga>, Int>> {
        return try {
            val response = api.searchManga(
                title = query,
                limit = limit,
                offset = offset,
                contentRatings =  listOf("safe", "suggestive", "erotica", "pornographic"),
                includes = listOf("cover_art")
            )

            val mangaList = response.data.mapNotNull { mangaDto ->
                val title = mangaDto.attributes?.title?.get("en")
                    ?: mangaDto.attributes?.title?.values?.firstOrNull()
                    ?: "Unknown Title"

                val coverFileName = mangaDto.relationships
                    .find { it.type == "cover_art" }
                    ?.attributes
                    ?.fileName

                SearchManga(
                    id = mangaDto.id,
                    title = title,
                    coverFileName = coverFileName
                )
            }
            Result.success(Pair(mangaList, response.total))
        } catch (e: Exception){
            Log.e(TAG, "Error searching manga", e)
            Result.failure(e)
        }
    }
}