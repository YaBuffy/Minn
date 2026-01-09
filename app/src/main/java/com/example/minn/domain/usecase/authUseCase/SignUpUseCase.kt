package com.example.minn.domain.usecase.authUseCase

import com.example.minn.Util.Response
import com.example.minn.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignUpUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password: String, name: String): Flow<Response<Boolean>>{
        return repository.signUp(email, password, name)
    }
}