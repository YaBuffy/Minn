package com.example.minn.domain.usecase.chatUseCase

import com.example.minn.domain.model.Message
import com.example.minn.domain.repository.ChatRepository
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class LoadLastMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): Pair<List<Message>, DocumentSnapshot?>{
        return repository.loadLastMessages(chatId)
    }
}