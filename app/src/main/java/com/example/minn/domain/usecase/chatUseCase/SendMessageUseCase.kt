package com.example.minn.domain.usecase.chatUseCase

import com.example.minn.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, text: String) {
        return repository.sendMessage(chatId, text)
    }
}