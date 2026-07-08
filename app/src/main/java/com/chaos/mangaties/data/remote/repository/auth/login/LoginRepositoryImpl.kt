package com.chaos.mangaties.data.remote.repository.auth.login

import com.chaos.mangaties.domain.repository.auth.login.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): LoginRepository{
    override suspend fun login(email: String, password: String): FirebaseUser {
        val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()

        return result.user ?: throw Exception("User login failed")
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun addAuthStateListener(listener: (FirebaseUser?) -> Unit) {
        firebaseAuth.addAuthStateListener { auth ->
            listener(auth.currentUser)
        }
    }
}