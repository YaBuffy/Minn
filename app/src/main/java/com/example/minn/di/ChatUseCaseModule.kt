package com.example.minn.di

import com.example.minn.domain.repository.ChatRepository
import com.example.minn.domain.usecase.chatUseCase.ObserveNewMessagesUseCase
import com.example.minn.domain.usecase.chatUseCase.GetOrCreateChatUseCase
import com.example.minn.domain.usecase.chatUseCase.LoadLastChatsUseCase
import com.example.minn.domain.usecase.chatUseCase.LoadLastMessagesUseCase
import com.example.minn.domain.usecase.chatUseCase.LoadOlderMessagesUseCase
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
    fun provideObserveNewMessagesUseCase(
        repository: ChatRepository
    ): ObserveNewMessagesUseCase {
        return ObserveNewMessagesUseCase(repository)
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

    @Provides
    @Singleton
    fun provideLoadOlderMessagesUseCase(
        repository: ChatRepository
    ): LoadOlderMessagesUseCase {
        return LoadOlderMessagesUseCase(repository)
    }
    @Provides
    @Singleton
    fun provideLoadLastMessagesUseCase(
        repository: ChatRepository
    ): LoadLastMessagesUseCase {
        return LoadLastMessagesUseCase(repository)
    }
    @Provides
    @Singleton
    fun provideLoadLastChatsUseCase(
        repository: ChatRepository
    ): LoadLastChatsUseCase {
        return LoadLastChatsUseCase(repository)
    }

}