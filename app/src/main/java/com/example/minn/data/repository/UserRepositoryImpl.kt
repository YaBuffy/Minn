package com.example.minn.data.repository

import com.example.minn.Util.Response
import com.example.minn.domain.model.User
import com.example.minn.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
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
                    "nameLower" to user.nameLower,
                    "surname" to user.surname,
                    "avatar" to user.avatar,
                    "bio" to user.bio,
                    "gender" to user.gender.name,
                    "birthdate" to user.birthdate
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

    override fun getAllUsers(): Flow<Response<List<User>>> = callbackFlow {
        trySend(Response.Loading)

        val currentUid = auth.currentUser?.uid

        val listener = firestore
            .collection("users")
            .addSnapshotListener { snapshot, error ->
                if(error != null){
                    trySend(Response.Error(error.message ?: "Error"))
                    return@addSnapshotListener
                }
                val users = snapshot?.documents
                    ?.mapNotNull { it.toObject(User::class.java) }
                    ?.filter { it.uid != currentUid }
                    ?: emptyList()

                trySend(Response.Success(users))
            }
        awaitClose { listener.remove() }
    }

    override fun searchUserByName(query: String): Flow<List<User>> = flow{

        val currentUid = auth.currentUser?.uid

        if(query.isBlank()){
            emit(emptyList())
            return@flow
        }

        val q = query.lowercase()

        val snapshot = firestore.collection("users")
            .orderBy("nameLower")
            .startAt(q)
            .endAt(q+ "\uf8ff")
            .get()
            .await()

        val users = snapshot.toObjects(User::class.java)
            .filter { it.uid != currentUid}

        emit(users)
    }
}