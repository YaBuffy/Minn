package com.example.minn.presentation.auth

import com.example.minn.testing.FakeAuthRepository
import com.example.minn.testing.FakeUserRepository
import com.example.minn.testing.MainDispatcherRule
import com.example.minn.domain.usecase.authUseCase.AuthUseCases
import com.example.minn.domain.usecase.authUseCase.GetAuthStateUseCase
import com.example.minn.domain.usecase.authUseCase.IsUserAuthUseCase
import com.example.minn.domain.usecase.authUseCase.SignInUseCase
import com.example.minn.domain.usecase.authUseCase.SignUpUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = FakeAuthRepository()
    private val userRepository = FakeUserRepository()

    private fun createViewModel(): AuthViewModel = AuthViewModel(
        AuthUseCases(
            isUserAuth = IsUserAuthUseCase(authRepository),
            getAuthState = GetAuthStateUseCase(authRepository),
            signIn = SignInUseCase(authRepository, userRepository),
            signUp = SignUpUseCase(authRepository),
        )
    )

    @Test
    fun `initial state is loading and unauthorized`() = runTest {
        val vm = createViewModel()

        // Before any auth state emission the ViewModel keeps its default state.
        assertTrue(vm.state.value.isLoading)
        assertFalse(vm.state.value.isAuthorized)
    }

    @Test
    fun `an unauthorized auth state ends the loading phase`() = runTest {
        authRepository.authState.value = false

        val vm = createViewModel()
        advanceUntilIdle()

        assertFalse(vm.state.value.isLoading)
        assertFalse(vm.state.value.isAuthorized)
    }

    @Test
    fun `authorized user reaches the chat list`() = runTest {
        authRepository.authState.value = true

        val vm = createViewModel()
        advanceUntilIdle()

        assertTrue(vm.state.value.isAuthorized)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `sign out is reflected in the auth state flow`() = runTest {
        authRepository.authState.value = true
        val vm = createViewModel()
        advanceUntilIdle()
        assertTrue(vm.state.value.isAuthorized)

        authRepository.authState.value = false
        advanceUntilIdle()

        assertFalse(vm.state.value.isAuthorized)
    }

    @Test
    fun `successful sign in sets the authorized flag`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.signIn("a@b.c", "secret")
        advanceUntilIdle()

        assertTrue(vm.state.value.isAuthorized)
        assertEquals(listOf(true), userRepository.onlineUpdates)
    }

    @Test
    fun `failed sign in surfaces the error`() = runTest {
        authRepository.signInResponse = kotlinx.coroutines.flow.flowOf(
            com.example.minn.Util.Response.Error("no such user")
        )
        val vm = createViewModel()
        advanceUntilIdle()

        vm.signIn("a@b.c", "bad")
        advanceUntilIdle()

        assertEquals("no such user", vm.state.value.error)
        assertFalse(vm.state.value.isAuthorized)
    }

    /**
     * Regression guard for a real behaviour of the current implementation:
     * `handleResponse` builds a brand new AuthUIState instead of copying the
     * previous one, so a successful request wipes any earlier error. Pinned here
     * so a future change to `copy(...)` is a deliberate decision, not silent.
     */
    @Test
    fun `a new request clears the previous error`() = runTest {
        authRepository.signInResponse = kotlinx.coroutines.flow.flowOf(
            com.example.minn.Util.Response.Error("first failure")
        )
        val vm = createViewModel()
        advanceUntilIdle()
        vm.signIn("a@b.c", "bad")
        advanceUntilIdle()
        assertEquals("first failure", vm.state.value.error)

        authRepository.signInResponse = kotlinx.coroutines.flow.flowOf(
            com.example.minn.Util.Response.Success(true)
        )
        vm.signIn("a@b.c", "secret")
        advanceUntilIdle()

        assertTrue(vm.state.value.isAuthorized)
        assertEquals(null, vm.state.value.error)
    }

    @Test
    fun `sign up passes the name through to the repository`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.signUp("a@b.c", "secret", "Ada")
        advanceUntilIdle()

        assertEquals(1, authRepository.signUpCalls)
        assertEquals("Ada", authRepository.lastSignUpName)
        assertTrue(vm.state.value.isAuthorized)
    }

    @Test
    fun `form fields are stored and replaced`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()

        vm.onNameChange("Ada")
        vm.onEmailChange("ada@b.c")
        vm.onPasswordChange("secret")

        assertEquals("Ada", vm.name)
        assertEquals("ada@b.c", vm.email)
        assertEquals("secret", vm.password)

        vm.onNameChange("Ada Lovelace")

        assertEquals("Ada Lovelace", vm.name)
    }
}