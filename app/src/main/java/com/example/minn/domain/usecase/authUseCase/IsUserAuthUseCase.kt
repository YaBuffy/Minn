package com.example.minn.domain.usecase.authUseCase

import com.example.minn.domain.repository.AuthRepository

class IsUserAuthUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): Boolean{
        return repository.isUserAuth()
    }
}