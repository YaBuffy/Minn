package com.example.minn.domain.repository

import com.example.minn.domain.model.Chat
import com.example.minn.domain.model.Message
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeNewMessages(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, text: String)
    suspend fun getOrCreateChat(otherUserId: String): Chat
    suspend fun loadLastMessages(chatId: String): Pair<List<Message>, DocumentSnapshot?>
    suspend fun loadOlderMessages(chatId: String, lastSnapshot: DocumentSnapshot?): Pair<List<Message>, DocumentSnapshot?>
}