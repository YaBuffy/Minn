package com.example.minn.domain.usecase.authUseCase

import com.example.minn.Util.Response
import com.example.minn.testing.FakeAuthRepository
import com.example.minn.testing.FakeUserRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SignInUseCaseTest {

    private val authRepository = FakeAuthRepository()
    private val userRepository = FakeUserRepository()
    private val useCase = SignInUseCase(authRepository, userRepository)

    @Test
    fun `successful sign in marks the user online`() = runTest {
        useCase("a@b.c", "secret").toList()

        assertEquals(listOf(true), userRepository.onlineUpdates)
    }

    @Test
    fun `failed sign in does not mark the user online`() = runTest {
        authRepository.signInResponse = flowOf(
            Response.Loading,
            Response.Error("bad password")
        )

        val emissions = useCase("a@b.c", "wrong").toList()

        assertTrue(userRepository.onlineUpdates.isEmpty())
        assertEquals("bad password", (emissions.last() as Response.Error).message)
    }

    @Test
    fun `emits loading before the outcome`() = runTest {
        val emissions = useCase("a@b.c", "secret").toList()

        assertTrue(emissions.first() is Response.Loading)
        assertTrue(emissions.last() is Response.Success)
    }

    /**
     * The repository emits its own Loading, and the use case deliberately drops
     * it (`Response.Loading -> {}`) instead of forwarding a second one.
     */
    @Test
    fun `does not re-emit the repository loading state`() = runTest {
        val emissions = useCase("a@b.c", "secret").toList()

        assertEquals(1, emissions.count { it is Response.Loading })
    }
}

class SignOutUseCaseTest {

    private val authRepository = FakeAuthRepository()
    private val userRepository = FakeUserRepository()
    private val useCase = SignOutUseCase(authRepository, userRepository)

    @Test
    fun `marks the user offline and signs out`() = runTest {
        useCase()

        assertEquals(listOf(false), userRepository.onlineUpdates)
        assertEquals(1, authRepository.signOutCalls)
    }

    }