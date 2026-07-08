package com.chaos.mangaties.domain.repository.manga

import com.chaos.mangaties.domain.model.manga.manga.SearchManga


interface SearchRepository {
    suspend fun searchManga(
        query: String,
        offset: Int,
        limit: Int,
    ) : Result<Pair<List<SearchManga>, Int>>
}