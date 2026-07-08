package com.chaos.mangaties.data.remote.repository.auth.forgetpassword

import android.util.Log
import com.chaos.mangaties.domain.repository.auth.forgetpassword.ForgetPasswordRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ForgetPasswordImpl @Inject constructor(
    private val auth : FirebaseAuth
) : ForgetPasswordRepository{

    private val TAG = "AuthRepository"

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email)
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to send password reset email", e)
                }
                .await()
            Result.success(Unit)
        } catch (e: Exception){
            Log.e(TAG, "Error sending password reset email", e)
            Result.failure(e)
        }
    }
}