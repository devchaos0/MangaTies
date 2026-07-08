package com.chaos.mangaties.domain.repository.manga

import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    suspend fun addFavourite(mangaId: String, userId: String): Result<Unit>
    suspend fun removeFavourite(mangaId: String, userId: String): Result<Unit>
    fun getFavourite(userId: String): Flow<List<String>>
    fun isFavourite(mangaId: String, userId: String): Flow<Boolean>
}
