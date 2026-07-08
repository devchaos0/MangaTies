package com.chaos.mangaties.domain.repository.statemanager

import kotlinx.coroutines.flow.Flow

interface AuthStateManager {
    fun isUserLoggedIn(): Flow<Boolean>
    suspend fun getCurrentUser(): User?
    suspend fun logout(): Result<Unit>
}

data class User(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?
)