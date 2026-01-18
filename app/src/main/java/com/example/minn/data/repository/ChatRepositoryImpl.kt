package com.example.minn.data.repository

import com.example.minn.domain.model.Chat
import com.example.minn.domain.model.Message
import com.example.minn.domain.repository.ChatRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ChatRepository {
    override fun getMessage(chatId: String): Flow<List<Message>> = callbackFlow{
        val listener = firestore
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val messages = snapshot?.toObjects(Message::class.java) ?: emptyList()
                trySend(messages)
            }
        awaitClose{listener.remove()}
    }

    override suspend fun sendMessage(chatId: String, text: String){
        val messageRef = firestore
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .document()

        val message = Message(
            id = messageRef.id,
            senderId = auth.currentUser!!.uid,
            text = text,
            timestamp = Timestamp.now()
        )

        firestore.runBatch {batch ->
            batch.set(messageRef, message)

            batch.update(
                firestore.collection("chats").document(chatId),
                mapOf(
                    "lastMessage" to text,
                    "lastMessageTimestamp" to Timestamp.now(),
                    "lastSenderId" to auth.currentUser!!.uid
                )
            )

        }.await()
    }

    override suspend fun getOrCreateChat(otherUserId: String): Chat {
        val currentUid = auth.currentUser!!.uid

        val chatId = listOf(currentUid, otherUserId)
            .sorted()
            .joinToString("_")

        val chat = Chat(
            id = chatId,
            members = listOf(currentUid, otherUserId),
            createdAt = Timestamp.now()
        )

        firestore
            .collection("chats")
            .document(chatId)
            .set(chat, SetOptions.merge())
            .await()

        return chat
    }

}