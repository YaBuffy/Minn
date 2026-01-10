package com.example.minn.di

import com.example.minn.domain.repository.AuthRepository
import com.example.minn.domain.repository.UserRepository
import com.example.minn.domain.usecase.authUseCase.AuthUseCases
import com.example.minn.domain.usecase.authUseCase.DeleteAccountUseCase
import com.example.minn.domain.usecase.authUseCase.GetAuthStateUseCase
import com.example.minn.domain.usecase.authUseCase.IsUserAuthUseCase
import com.example.minn.domain.usecase.authUseCase.ReauthenticateUseCase
import com.example.minn.domain.usecase.authUseCase.SignInUseCase
import com.example.minn.domain.usecase.authUseCase.SignOutUseCase
import com.example.minn.domain.usecase.authUseCase.SignUpUseCase
import com.example.minn.domain.usecase.userUseCase.UpdateUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
        )
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        repository: AuthRepository
    ): SignOutUseCase {
        return SignOutUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideReauthenticateUseCase(
        repository: AuthRepository
    ): ReauthenticateUseCase {
        return ReauthenticateUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(
        repository: AuthRepository
    ): DeleteAccountUseCase {
        return DeleteAccountUseCase(repository)
    }
}