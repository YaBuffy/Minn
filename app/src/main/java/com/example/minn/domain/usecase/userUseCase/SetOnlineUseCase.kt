package com.example.minn.domain.usecase.userUseCase

import com.example.minn.domain.repository.UserRepository
import javax.inject.Inject

class SetOnlineUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(isOnline: Boolean){
        return repository.setOnline(isOnline)
    }
}