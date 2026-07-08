package com.chaos.mangaties.data.remote.repository.auth.signup

import android.util.Log
import com.chaos.mangaties.domain.repository.auth.signup.SignUpRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : SignUpRepository{
    override suspend fun signUp(email: String, password: String): FirebaseUser {
        val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
        result.user?.let { user ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    user.sendEmailVerification().await()
                } catch (e: Exception) {
                    Log.e("SignUp", "Failed to send verification email", e)
                }
            }
            return user
        } ?: throw Exception("User creation failed")
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