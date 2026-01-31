package com.example.minn.domain.usecase.chatUseCase

import com.example.minn.domain.model.Chat
import com.example.minn.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadLastChatsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<List<Chat>> {
        return repository.loadLastChats()
    }
}