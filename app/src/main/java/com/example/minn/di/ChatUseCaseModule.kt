package com.example.minn.di

import com.example.minn.domain.repository.ChatRepository
import com.example.minn.domain.usecase.chatUseCase.GetMessageUseCase
import com.example.minn.domain.usecase.chatUseCase.GetOrCreateChatUseCase
import com.example.minn.domain.usecase.chatUseCase.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ChatUseCaseModule {

    @Provides
    @Singleton
    fun provideGetMessageUseCase(
        repository: ChatRepository
    ): GetMessageUseCase {
        return GetMessageUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSendMessageUseCase(
        repository: ChatRepository
    ): SendMessageUseCase {
        return SendMessageUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetOrCreateChatUseCase(
        repository: ChatRepository
    ): GetOrCreateChatUseCase {
        return GetOrCreateChatUseCase(repository)
    }

}