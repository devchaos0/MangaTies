package com.chaos.mangaties.domain.repository.auth.signup

import com.google.firebase.auth.FirebaseUser

interface SignUpRepository{
    suspend fun signUp(email: String, password: String): FirebaseUser
    fun getCurrentUser(): FirebaseUser?
    fun addAuthStateListener(listener: (FirebaseUser?) -> Unit)
}