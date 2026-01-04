package com.example.minn.domain.usecase.userUseCase

import com.example.minn.domain.model.User
import com.example.minn.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User){
        return repository.updateUser(user)
    }
}