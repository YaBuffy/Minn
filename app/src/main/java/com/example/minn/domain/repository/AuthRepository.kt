package com.example.minn.domain.repository

import com.example.minn.Util.Response
import kotlinx.coroutines.flow.Flow


interface AuthRepository {


    fun isUserAuth(): Boolean

    fun getAuthState(): Flow<Boolean>

    fun signUp(email: String, password: String): Flow<Response<Boolean>>

    fun signIn(email: String, password: String): Flow<Response<Boolean>>

    fun signOut(): Flow<Response<Boolean>>
}