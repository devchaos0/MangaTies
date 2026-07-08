package com.chaos.mangaties.domain.model.manga.manga

data class FavouriteManga(
    val mangaId: String,
    val title: String,
    val coverFileName: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)