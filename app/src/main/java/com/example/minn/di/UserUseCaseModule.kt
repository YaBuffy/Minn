package com.example.minn.di

import com.example.minn.domain.repository.UserRepository
import com.example.minn.domain.usecase.userUseCase.CreateUserUseCase
import com.example.minn.domain.usecase.userUseCase.GetUserUseCase
import com.example.minn.domain.usecase.userUseCase.UpdateUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Provides
    @Singleton
    fun provideCreateUserUseCase(
        repository: UserRepository
    ): CreateUserUseCase{
        return CreateUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserUseCase(
        repository: UserRepository
    ): UpdateUserUseCase {
        return UpdateUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(
        repository: UserRepository
    ): GetUserUseCase {
        return GetUserUseCase(repository)
    }

}