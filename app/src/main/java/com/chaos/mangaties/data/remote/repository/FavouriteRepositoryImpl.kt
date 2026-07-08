package com.chaos.mangaties.data.remote.repository

import android.util.Log
import com.chaos.mangaties.domain.repository.manga.FavouriteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FavouriteRepository {

    private val TAG = "FavouriteRepository"
    private val COLLECTION_NAME = "favourites"

    override suspend fun addFavourite(mangaId: String, userId: String): Result<Unit> {
        return try {
            val favouriteData = hashMapOf(
                "mangaId" to mangaId,
                "userId" to userId,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection(COLLECTION_NAME)
                .document("${userId}_${mangaId}")
                .set(favouriteData)
                .await()

            Log.d(TAG, "Added favourite: $mangaId for user: $userId")
            Result.success(Unit)
        } catch (e: FirebaseFirestoreException) {
            if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                Log.e(TAG, "Permission denied for adding favourite", e)
            } else {
                Log.e(TAG, "Error adding favourite", e)
            }
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding favourite", e)
            Result.failure(e)
        }
    }

    override suspend fun removeFavourite(mangaId: String, userId: String): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_NAME)
                .document("${userId}_${mangaId}")
                .delete()
                .await()

            Log.d(TAG, "Removed favourite: $mangaId for user: $userId")
            Result.success(Unit)
        } catch (e: FirebaseFirestoreException) {
            if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                Log.e(TAG, "Permission denied for removing favourite", e)
            } else {
                Log.e(TAG, "Error removing favourite", e)
            }
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error removing favourite", e)
            Result.failure(e)
        }
    }

    override fun getFavourite(userId: String): Flow<List<String>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_NAME)
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    if (error is FirebaseFirestoreException && error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        Log.e(TAG, "Permission denied for getting favourites", error)
                    }
                    close(error)
                    return@addSnapshotListener
                }

                val favourites = snapshots?.documents?.mapNotNull { doc ->
                    doc.getString("mangaId")
                } ?: emptyList()
                
                trySend(favourites)
            }

        awaitClose {
            listener.remove()
        }
    }

    override fun isFavourite(mangaId: String, userId: String): Flow<Boolean> = callbackFlow {
        val listener = firestore.collection(COLLECTION_NAME)
            .document("${userId}_${mangaId}")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    if (error is FirebaseFirestoreException && error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        Log.e(TAG, "Permission denied for checking favourite status", error)
                    }
                    close(error)
                    return@addSnapshotListener
                }
                
                trySend(snapshot?.exists() ?: false)
            }

        awaitClose {
            listener.remove()
        }
    }
}
