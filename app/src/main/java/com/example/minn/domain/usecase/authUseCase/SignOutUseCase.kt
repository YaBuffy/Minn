package com.example.minn.domain.usecase.authUseCase

import com.example.minn.domain.repository.AuthRepository
import com.example.minn.domain.repository.UserRepository

class SignOutUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(){
        userRepository.setOnline(false)
        authRepository.signOut()
    }
}