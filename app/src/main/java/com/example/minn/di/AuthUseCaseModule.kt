package com.example.minn.di

import com.example.minn.domain.repository.AuthRepository
import com.example.minn.domain.usecase.AuthUseCases
import com.example.minn.domain.usecase.GetAuthStateUseCase
import com.example.minn.domain.usecase.IsUserAuthUseCase
import com.example.minn.domain.usecase.SignInUseCase
import com.example.minn.domain.usecase.SignOutUseCase
import com.example.minn.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {

    @Provides
    fun provideAuthUseCases(
        repository: AuthRepository
    ): AuthUseCases{
        return AuthUseCases(
            isUserAuth = IsUserAuthUseCase(repository),
            getAuthState = GetAuthStateUseCase(repository),
            signIn = SignInUseCase(repository),
            signUp = SignUpUseCase(repository),
            signOut = SignOutUseCase(repository),
        )
    }
}