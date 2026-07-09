package com.chaos.mangaties.data.remote.dto.manga


import kotlinx.serialization.Serializable

@Serializable
data class MangaResponseDtoModel(
    val result: String,
    val response: String,
    val data: List<MangaDataDto>,
    val limit: Int,
    val offset: Int,
    val total: Int
)

@Serializable
data class MangaDataDto(
    val id: String,
    val type: String,
    val attributes: MangaAttributesDto,
    val relationships: List<RelationshipDto>
)

@Serializable
data class MangaAttributesDto(
    val title: Map<String, String>,
    val altTitles: List<Map<String, String>>?,
    val description: Map<String, String>?,
    val isLocked: Boolean,
    val links: Map<String, String>?,
    val originalLanguage: String,
    val lastVolume: String?,
    val lastChapter: String?,
    val publicationDemographic: String?,
    val status: String,
    val year: Int?,
    val contentRating: String,
    val tags: List<TagDto>,
    val state: String,
    val chapterNumbersResetOnNewVolume: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val version: Int,
    val availableTranslatedLanguages: List<String>,
    val latestUploadedChapter: String?
)

@Serializable
data class RelationshipDto(
    val id: String,
    val type: String,
    val attributes: CoverArtAttributesDto? = null
)

@Serializable
data class CoverArtAttributesDto(
    val description: String? = null,
    val volume: String? = null,
    val fileName: String? = null,
    val locale: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val version: Int? = null
)
