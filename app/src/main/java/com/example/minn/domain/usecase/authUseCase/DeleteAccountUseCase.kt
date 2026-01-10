package com.example.minn.domain.usecase.authUseCase

import com.example.minn.Util.Response
import com.example.minn.domain.repository.AuthRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Response<Boolean>{
        return repository.deleteAccount()
    }
}