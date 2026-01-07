package com.example.minn.data.repository

import com.example.minn.Util.Response
import com.example.minn.domain.model.User
import com.example.minn.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): UserRepository {
    override suspend fun createUser(user: User) {
        firestore.collection("users")
            .document(user.uid)
            .set(user)
            .await()
    }

    override suspend fun updateUser(user: User) {
        firestore.collection("users")
            .document(user.uid)
            .update(
                mapOf(
                    "name" to user.name,
                    "surname" to user.surname,
                    "avatar" to user.avatar,
                    "bio" to user.bio,
                    "gender" to user.gender.name
                )
            ).await()
    }

    override fun getUser(uid: String): Flow<Response<User>> = callbackFlow {
        trySend(Response.Loading)

        val listener = firestore
            .collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                if(error != null){
                    trySend(Response.Error(error.message ?: "Error"))
                    return@addSnapshotListener
                }

                val user = snapshot?.toObject(User::class.java)
                if(user != null){
                    trySend(Response.Success(user))
                }
            }
        awaitClose{listener.remove()}
    }
}