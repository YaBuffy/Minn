package com.example.minn.domain.repository

import com.example.minn.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessage(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, text: String)
    suspend fun getOrCreateChat(otherUserId: String): String
}