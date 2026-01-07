package com.example.minn.domain.usecase.authUseCase

import com.example.minn.domain.repository.AuthRepository

class SignOutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(){
        return repository.signOut()
    }
}