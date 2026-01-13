package com.example.minn.domain.usecase.userUseCase

import com.example.minn.domain.model.User
import com.example.minn.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUserByNameUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(query: String): Flow<List<User>>{
        return repository.searchUserByName(query)
    }
}