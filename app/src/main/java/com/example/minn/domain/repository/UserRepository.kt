package com.example.minn.domain.repository

import com.example.minn.Util.Response
import com.example.minn.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun updateUser(user: User)
}