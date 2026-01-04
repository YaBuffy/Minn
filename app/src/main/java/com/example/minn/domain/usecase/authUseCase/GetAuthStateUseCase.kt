package com.example.minn.domain.usecase.authUseCase

import com.example.minn.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetAuthStateUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean>{
        return repository.getAuthState()
    }
}