package com.example.minn.domain.usecase

import com.example.minn.Util.Response
import com.example.minn.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignOutUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Response<Boolean>>{
        return repository.signOut()
    }
}