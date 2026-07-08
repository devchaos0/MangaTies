package com.chaos.mangaties.domain.repository.auth.login

import com.google.firebase.auth.FirebaseUser

interface LoginRepository {
    suspend fun login(email: String, password: String) : FirebaseUser
    fun getCurrentUser() : FirebaseUser?
    fun addAuthStateListener(listener : (FirebaseUser?) -> Unit)
}