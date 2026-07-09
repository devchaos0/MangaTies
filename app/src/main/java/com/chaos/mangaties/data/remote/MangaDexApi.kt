package com.chaos.mangaties.data.remote

import com.chaos.mangaties.data.remote.dto.manga.ChapterPagesResponseDto
import com.chaos.mangaties.data.remote.dto.manga.MangaChapterResponseDto
import com.chaos.mangaties.data.remote.dto.manga.MangaDetailResponseDto
import com.chaos.mangaties.data.remote.dto.manga.MangaResponseDtoModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaDexApi {

    /**
     * Get a list of manga with optional filters
     * Default: Returns manga that have English chapters available
     */
    @GET("manga")
    suspend fun getMangaList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("title") title: String? = null,
        @Query("includedTags[]") includedTags: List<String>? = null,
        @Query("excludedTags[]") excludedTags: List<String>? = null,
        @Query("includedTagsMode") includedTagsMode: String = "AND",
        @Query("excludedTagsMode") excludedTagsMode: String = "OR",
        @Query("status[]") status: List<String>? = null,
        @Query("publicationDemographic[]") publicationDemographic: List<String>? = null,
        @Query("contentRating[]") contentRatings: List<String> = listOf("safe", "suggestive", "erotica","pornographic"),
        @Query("availableTranslatedLanguage[]") availableTranslatedLanguages: List<String> = listOf("en"),
        @Query("order[latestUploadedChapter]") order: String = "desc",
        @Query("includes[]") includes: List<String> = listOf("cover_art"),
        @Query("hasAvailableChapters") hasAvailableChapters: String? = null,
        @Query("createdAtSince") createdAtSince: String? = null,
        @Query("updatedAtSince") updatedAtSince: String? = null
    ): MangaResponseDtoModel

    /**
     * Get detailed information about a specific manga
     */
    @GET("manga/{id}")
    suspend fun getMangaDetails(
        @Path("id") id: String,
        @Query("includes[]") includes: List<String> = listOf("cover_art", "author", "artist"),
        @Query("contentRating[]") contentRatings: List<String>? = null
    ): MangaDetailResponseDto

    /**
     * Get chapters for a specific manga
     * Default: Returns ONLY English chapters
     */
    @GET("manga/{id}/feed")
    suspend fun getMangaChapters(
        @Path("id") id: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("translatedLanguage[]") translatedLanguage: List<String> = listOf("en"),
        @Query("originalLanguage[]") originalLanguage: List<String>? = null,
        @Query("excludedOriginalLanguage[]") excludedOriginalLanguage: List<String>? = null,
        @Query("contentRating[]") contentRatings: List<String> = listOf("safe", "suggestive", "erotica", "pornographic"),
        @Query("excludedGroups[]") excludedGroups: List<String>? = null,
        @Query("excludedUploaders[]") excludedUploaders: List<String>? = null,
        @Query("includeFutureUpdates") includeFutureUpdates: String = "1",
        @Query("externalUrl") externalUrl: String? = null,
        @Query("excludeExternalUrl") excludeExternalUrl: String? = null,
        @Query("createdAtSince") createdAtSince: String? = null,
        @Query("updatedAtSince") updatedAtSince: String? = null,
        @Query("publishAtSince") publishAtSince: String? = null,
        @Query("order[createdAt]") orderCreatedAt: String? = null,
        @Query("order[updatedAt]") orderUpdatedAt: String? = null,
        @Query("order[publishAt]") orderPublishAt: String? = null,
        @Query("order[readableAt]") orderReadableAt: String? = null,
        @Query("order[volume]") orderVolume: String = "asc",
        @Query("order[chapter]") orderChapter: String = "asc",
        @Query("includes[]") includes: List<String> = listOf("manga", "scanlation_group", "user"),
        @Query("includeEmptyPages") includeEmptyPages: Int? = null,
        @Query("includeFuturePublishAt") includeFuturePublishAt: Int? = null,
        @Query("includeExternalUrl") includeExternalUrl: Int? = null,
        @Query("includeUnavailable") includeUnavailable: String = "0"
    ): MangaChapterResponseDto

    /**
     * Search for manga by title
     * Default: Returns ONLY manga with English chapters
     */
    @GET("manga")
    suspend fun searchManga(
        @Query("title") title: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("includedTags[]") includedTags: List<String>? = null,
        @Query("excludedTags[]") excludedTags: List<String>? = null,
        @Query("includedTagsMode") includedTagsMode: String = "AND",
        @Query("excludedTagsMode") excludedTagsMode: String = "OR",
        @Query("status[]") status: List<String>? = null,
        @Query("publicationDemographic[]") publicationDemographic: List<String>? = null,
        @Query("contentRating[]") contentRatings: List<String> = listOf("safe", "suggestive", "erotica", "pornographic"),
        @Query("availableTranslatedLanguage[]") availableTranslatedLanguages: List<String> = listOf("en"),
        @Query("order[latestUploadedChapter]") order: String = "desc",
        @Query("includes[]") includes: List<String> = listOf("cover_art"),
        @Query("hasAvailableChapters") hasAvailableChapters: String? = null,
        @Query("createdAtSince") createdAtSince: String? = null,
        @Query("updatedAtSince") updatedAtSince: String? = null
    ): MangaResponseDtoModel

    /**
     * Get image server info for a specific chapter
     * Note: Do NOT send Authorization header when fetching images!
     */
    @GET("at-home/server/{chapterId}")
    suspend fun getChapterPages(
        @Path("chapterId") chapterId: String,
        @Query("forcePort443") forcePort443: Boolean = true
    ): ChapterPagesResponseDto
}