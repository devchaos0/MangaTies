package com.chaos.mangaties.data.remote.dto.manga

import kotlinx.serialization.Serializable

@Serializable
data class ChapterPagesResponseDto(
    val result: String,
    val baseUrl: String,
    val chapter: ChapterDataDto
)

@Serializable
data class ChapterDataDto(
    val hash: String,
    val data: List<String>,
    val dataSaver: List<String>
)