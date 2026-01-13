package com.example.minn.domain.usecase.userUseCase

import com.example.minn.Util.Response
import com.example.minn.domain.model.User
import com.example.minn.domain.repository.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Response<List<User>>>{
        return repository.getAllUsers()
    }
}