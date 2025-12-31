package com.example.minn.domain.usecase

data class AuthUseCases(
    val isUserAuth: IsUserAuthUseCase,
    val getAuthState: GetAuthStateUseCase,
    val signIn: SignInUseCase,
    val signUp: SignUpUseCase,
    val signOut: SignOutUseCase
)