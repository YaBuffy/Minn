package com.example.minn.domain.usecase.chatUseCase

import com.example.minn.domain.model.Chat
import com.example.minn.domain.repository.ChatRepository
import javax.inject.Inject

class GetOrCreateChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(otherUserId: String): Chat {
        return repository.getOrCreateChat(otherUserId)
    }
}