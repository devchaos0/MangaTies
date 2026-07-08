package com.chaos.mangaties.domain.repository.manga

import com.chaos.mangaties.domain.model.manga.home.MangaHome
import com.chaos.mangaties.domain.model.manga.manga.ChapterPages
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import com.chaos.mangaties.domain.model.manga.manga.MangaDetails

interface MangaRepository {
    suspend fun getMangaList(offset: Int, limit: Int) : Result<List<MangaHome>>
    suspend fun getMangaDetails(mangaId: String): Result<MangaDetails>
    suspend fun getMangaChapters(mangaId: String, offset: Int, limit: Int) : Result<List<MangaChapter>>
    suspend fun getChapterPages(chapterId: String): Result<ChapterPages>
}