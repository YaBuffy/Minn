package com.example.minn.domain.usecase.authUseCase

import com.example.minn.Util.Response
import com.example.minn.domain.repository.AuthRepository
import javax.inject.Inject

class ReauthenticateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Response<Boolean>{
        return repository.reauthenticate(email,password)
    }
}