package com.chaos.mangaties.data.remote.dto.manga


import kotlinx.serialization.Serializable

@Serializable
data class MangaChapterResponseDto(
    val result: String,
    val response: String,
    val data: List<MangaChapterDataDto>,
    val limit: Int,
    val offset: Int,
    val total: Int
)

@Serializable
data class MangaChapterDataDto(
    val id: String,
    val type: String,
    val attributes: MangaChapterAttributesDto
)

@Serializable
data class MangaChapterAttributesDto(
    val volume: String? = null,
    val chapter: String,
    val title: String? = null,
    val translatedLanguage: String,
    val externalUrl: String? = null,
    val isUnavailable: Boolean = false,
    val publishAt: String,
    val readableAt: String,
    val createdAt: String,
    val updatedAt: String,
    val version: Int,
    val pages: Int = 0
)