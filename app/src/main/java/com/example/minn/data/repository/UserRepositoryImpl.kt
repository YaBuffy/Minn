package com.example.minn.data.repository

import com.example.minn.domain.model.User
import com.example.minn.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
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
                    "bio" to user.bio
                )
            ).await()
    }


}