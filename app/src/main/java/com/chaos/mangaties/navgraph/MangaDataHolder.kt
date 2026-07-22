package com.chaos.mangaties.navgraph

import com.chaos.mangaties.domain.model.manga.manga.DownloadedChapter
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter

object MangaDataHolder {
    var chapters: List<MangaChapter> = emptyList()
    var downloadedChapters: List<DownloadedChapter> = emptyList()
    var currentChapter: DownloadedChapter? = null
    var currentMangaId: String = ""
    var currentMangaTitle: String = ""
    var currentChapterIndex: Int = 0

    fun getChapterAt(index: Int): DownloadedChapter? {
        return downloadedChapters.getOrNull(index)
    }

    fun getNextChapter(): DownloadedChapter? {
        val nextIndex = currentChapterIndex + 1
        return if (nextIndex < downloadedChapters.size) {
            currentChapterIndex = nextIndex
            downloadedChapters[nextIndex]
        } else null
    }

    fun getPreviousChapter(): DownloadedChapter? {
        val prevIndex = currentChapterIndex - 1
        return if (prevIndex >= 0 && prevIndex < downloadedChapters.size) {
            currentChapterIndex = prevIndex
            downloadedChapters[prevIndex]
        } else null
    }

}