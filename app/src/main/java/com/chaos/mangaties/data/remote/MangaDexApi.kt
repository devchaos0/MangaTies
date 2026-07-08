package com.chaos.mangaties.data.remote

import com.chaos.mangaties.data.remote.dto.manga.ChapterPagesResponseDto
import com.chaos.mangaties.data.remote.dto.manga.MangaChapterResponseDto
import com.chaos.mangaties.data.remote.dto.manga.MangaDetailResponseDto
import retrofit2.http.Query
import com.chaos.mangaties.data.remote.dto.manga.MangaResponseDtoModel
import retrofit2.http.GET
import retrofit2.http.Path

interface MangaDexApi {
    @GET("manga")
    suspend fun getMangaList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("includedTagsMode") includedTagsMode: String = "AND",
        @Query("excludedTagsMode") excludedTagsMode: String = "OR",
        @Query("contentRating[]") contentRatings: List<String> = listOf("safe", "suggestive", "erotica", "pornographic"),
        @Query("order[latestUploadedChapter]") order: String = "desc",
        @Query("includes[]") includes: List<String> = listOf("cover_art")
    ) : MangaResponseDtoModel

    @GET("manga/{id}")
    suspend fun getMangaDetails(
        @Path("id") id: String,
        @Query("includes[]") includes: List<String> = listOf("cover_art")
    ) : MangaDetailResponseDto

    @GET("manga/{id}/feed")
    suspend fun getMangaChapters(
        @Path("id") id: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("contentRating[]") contentRatings: List<String> = listOf("safe", "suggestive", "erotica", "pornographic"),
        @Query("includeFutureUpdates") includeFutureUpdates: Int = 1,
        @Query("order[volume]") orderVolume: String = "asc",
        @Query("order[chapter]") orderChapter: String = "asc",
        @Query("includeUnavailable") includeUnavailable: Int = 0
    ): MangaChapterResponseDto

    @GET("manga")
    suspend fun searchManga(
        @Query("title") title: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("includedTagsMode") includedTagsMode: String = "AND",
        @Query("excludedTagsMode") excludedTagsMode: String = "OR",
        @Query("contentRating[]") contentRatings: List<String> = listOf("safe", "suggestive", "erotica", "pornographic"),
        @Query("order[latestUploadedChapter]") order: String = "desc",
        @Query("includes[]") includes: List<String> = listOf("cover_art")
    ): MangaResponseDtoModel


    @GET("at-home/server/{chapterId}")
    suspend fun getChapterPages(
        @Path("chapterId") chapterId: String,
        @Query("forcePort443") forcePort443: Boolean = true
    ) : ChapterPagesResponseDto

}
