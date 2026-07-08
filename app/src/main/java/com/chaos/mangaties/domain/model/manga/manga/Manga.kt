package com.chaos.mangaties.domain.model.manga.manga

import com.chaos.mangaties.data.remote.dto.manga.TagDto
import kotlinx.serialization.Serializable

@Serializable
data class MangaChapter(
    val id: String,
    val chapter: String,
    val title: String?,
    val volume: String?,
    val pages: Int,
    val updatedAt: String,
)

@Serializable
data class ChapterPages(
    val chapterId: String,
    val chapterNumber: String,
    val baseUrl: String,
    val hash: String,
    val data: List<String>,
    val dataSaver: List<String>,
)

@Serializable
data class MangaDetails(
    val id: String,
    val title: String,
    val description: String?,
    val status: String,
    val contentRating: String,
    val tags: List<TagDto>,
    val coverFileName: String?,
    val year: Int?,
    val originalLanguage: String,
    val publicationDemographic: String?,
    val chapters: List<MangaChapter> = emptyList()
)