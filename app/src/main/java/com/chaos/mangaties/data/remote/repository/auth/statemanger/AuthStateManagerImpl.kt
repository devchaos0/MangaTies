package com.chaos.mangaties.data.remote.repository.auth.statemanger


import android.util.Log
import com.chaos.mangaties.domain.repository.statemanager.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStateManagerImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthStateManager {

    private val TAG = "AuthStateManager"

    override fun isUserLoggedIn(): Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            trySend(user != null)
        }
        auth.addAuthStateListener(listener)

        // Send initial state
        trySend(auth.currentUser != null)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            mapFirebaseUserToUser(firebaseUser)
        } else {
            null
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error logging out", e)
            Result.failure(e)
        }
    }

    private fun mapFirebaseUserToUser(firebaseUser: FirebaseUser): User {
        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email,
            displayName = firebaseUser.displayName,
            photoUrl = firebaseUser.photoUrl?.toString()
        )
    }
}