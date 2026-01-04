package com.example.minn.domain.usecase.authUseCase

import com.example.minn.Util.Response
import com.example.minn.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignInUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Response<Boolean>>{
        return repository.signIn(email,password)
    }
}