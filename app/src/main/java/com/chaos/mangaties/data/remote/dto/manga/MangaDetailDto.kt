package com.chaos.mangaties.data.remote.dto.manga

import kotlinx.serialization.Serializable

@Serializable
data class MangaDetailResponseDto(
    val result: String,
    val response: String,
    val data: MangaDetailDataDto
)

@Serializable
data class MangaDetailDataDto(
    val id: String,
    val type: String,
    val attributes: MangaDetailAttributesDto,
    val relationships: List<RelationshipDto>
)

@Serializable
data class MangaDetailAttributesDto(
    val title: Map<String, String>,
    val altTitles: List<Map<String, String>>? = null,
    val description: Map<String, String>? = null,
    val isLocked: Boolean = false,
    val links: Map<String, String>? = null,
    val originalLanguage: String,
    val lastVolume: String? = null,
    val lastChapter: String? = null,
    val publicationDemographic: String? = null,
    val status: String,
    val year: Int? = null,
    val contentRating: String,
    val tags: List<TagDto> = emptyList(),
    val state: String,
    val chapterNumbersResetOnNewVolume: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
    val version: Int,
    val availableTranslatedLanguages: List<String> = emptyList(),
    val latestUploadedChapter: String? = null
)
