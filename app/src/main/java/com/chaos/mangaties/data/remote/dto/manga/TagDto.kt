package com.chaos.mangaties.data.remote.dto.manga

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val id: String,
    val type: String,
    val attributes: TagAttributesDto
)

@Serializable
data class TagAttributesDto(
    val name: Map<String, String>,
    val description: Map<String, String>? = null,
    val group: String,
    val version: Int
)
