package com.example.minn.domain.usecase.authUseCase

data class AuthUseCases(
    val isUserAuth: IsUserAuthUseCase,
    val getAuthState: GetAuthStateUseCase,
    val signIn: SignInUseCase,
    val signUp: SignUpUseCase,
)