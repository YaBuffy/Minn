package com.example.minn.domain.repository

import com.example.minn.Util.Response
import com.example.minn.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun updateUser(user: User)

    fun getUser(uid: String): Flow<Response<User>>

    fun getAllUsers(): Flow<Response<List<User>>>

    fun searchUserByName(query: String): Flow<List<User>>
    suspend fun setOnline(online: Boolean)
}