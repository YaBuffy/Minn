package com.example.minn.domain.usecase.authUseCase

import com.example.minn.Util.Response
import com.example.minn.domain.repository.AuthRepository
import com.example.minn.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(email: String, password: String): Flow<Response<Boolean>> = flow{
        emit(Response.Loading)
        authRepository.signIn(email,password).collect { response ->
            when(response){
                is Response.Success -> {
                    userRepository.setOnline(true)
                    emit(Response.Success(true))
                }
                is Response.Error -> emit(response)
                is Response.Loading -> {}
            }

        }
    }
}