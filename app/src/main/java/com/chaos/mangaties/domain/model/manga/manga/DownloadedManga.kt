package com.chaos.mangaties.domain.model.manga.manga

data class DownloadedManga(
    val mangaId: String,
    val mangaTitle: String,
    val coverFileName: String? = null,
    val downLoadedChapters: List<DownloadedChapter> = emptyList()
)

data class DownloadedChapter(
    val chapterId: String,
    val chapterNumber: String,
    val title: String? = null,
    val volume: String? = null,
    val pages: List<String> = emptyList(),
    val downloadedAt: Long = System.currentTimeMillis()
)