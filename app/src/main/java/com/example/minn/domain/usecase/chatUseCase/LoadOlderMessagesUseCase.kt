package com.example.minn.domain.usecase.chatUseCase

import com.example.minn.domain.model.Message
import com.example.minn.domain.repository.ChatRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class LoadOlderMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, lastSnapshot: DocumentSnapshot?): Pair<List<Message>, DocumentSnapshot?> {
        return repository.loadOlderMessages(chatId, lastSnapshot)
    }
}