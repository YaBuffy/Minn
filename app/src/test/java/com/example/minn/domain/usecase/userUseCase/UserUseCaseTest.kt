package com.example.minn.domain.usecase.userUseCase

import com.example.minn.domain.model.User
import com.example.minn.testing.FakeAuthRepository
import com.example.minn.testing.FakeUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateUserUseCaseTest {

    private val userRepository = FakeUserRepository()
    private val useCase = CreateUserUseCase(userRepository)

    @Test
    fun `passes the user straight through to the repository`() = runTest {
        val user = User(uid = "u1", name = "Ada", nameLower = "ada")

        useCase(user)

        assertEquals(listOf(user), userRepository.createdUsers)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllUsersUseCaseTest {

    private val userRepository = FakeUserRepository()
    private val useCase = GetAllUsersUseCase(userRepository)

    @Test
    fun `surfaces the loading state first`() = runTest {
        userRepository.allUsersResponse.value =
            com.example.minn.Util.Response.Loading

        val emissions = useCase().take(1).toList()

        assertTrue(emissions.first() is com.example.minn.Util.Response.Loading)
    }

    @Test
    fun `excludes nobody at this layer`() = runTest {
        val bob = User(uid = "bob", name = "Bob")
        userRepository.allUsersResponse.value =
            com.example.minn.Util.Response.Success(listOf(bob))

        val result = useCase().first { it is com.example.minn.Util.Response.Success }

        // Filtering the current user happens in UserRepositoryImpl, not here.
        assertEquals(listOf(bob), (result as com.example.minn.Util.Response.Success).data)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class SetOnlineUseCaseTest {

    private val userRepository = FakeUserRepository()
    private val useCase = SetOnlineUseCase(userRepository)

    @Test
    fun `records the online flag`() = runTest {
        useCase(true)
        useCase(false)

        assertEquals(listOf(true, false), userRepository.onlineUpdates)
    }
}