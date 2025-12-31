package com.example.minn.di

import com.example.minn.data.repository.AuthRepositoryImpl
import com.example.minn.domain.repository.AuthRepository
import dagger.Binds

abstract class AuthRepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}