package com.example.minn.domain.usecase.chatUseCase

import com.example.minn.domain.model.Message
import com.example.minn.domain.repository.ChatRepository
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNewMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>>{
        return repository.observeNewMessages(chatId)
    }
}