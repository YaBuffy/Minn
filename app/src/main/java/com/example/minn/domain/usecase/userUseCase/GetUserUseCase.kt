package com.example.minn.domain.usecase.userUseCase

import com.example.minn.Util.Response
import com.example.minn.domain.model.User
import com.example.minn.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(uid: String): Flow<Response<User>> {
        return repository.getUser(uid)
    }
}