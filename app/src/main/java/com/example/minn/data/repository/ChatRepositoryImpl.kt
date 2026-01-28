package com.example.minn.data.repository

import android.util.Log
import com.example.minn.domain.model.Chat
import com.example.minn.domain.model.Message
import com.example.minn.domain.repository.ChatRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
    private val pageSize = 20L
    override suspend fun loadLastMessages(
        chatId: String,
    ): Pair<List<Message>, DocumentSnapshot?> {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(pageSize)
            .get()
            .await()

        val messages = snapshot
            .toObjects(Message::class.java)
        val oldestSnapshot = snapshot.documents.lastOrNull()

        return messages to oldestSnapshot
    }

    override fun observeNewMessages(chatId: String): Flow<List<Message>>  = callbackFlow{
        val listener = firestore
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                trySend(snapshot.toObjects(Message::class.java))
            }
        awaitClose{listener.remove()}
    }

    override suspend fun loadOlderMessages(
        chatId: String,
        lastSnapshot: DocumentSnapshot?
    ): Pair<List<Message>, DocumentSnapshot?> {
        Log.d("Firebase", "Loading older from cursor: ${lastSnapshot?.id} (${lastSnapshot?.get("timestamp")})")
        if (lastSnapshot == null) {
            return emptyList<Message>() to null
        }
        val snapshot = FirebaseFirestore.getInstance()
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .startAfter(lastSnapshot)
            .limit(pageSize)
            .get()
            .await()

        val messages = snapshot.toObjects(Message::class.java)
        val newCursor = snapshot.documents.lastOrNull()  // сохраняем курсор
        Log.d("Firebase", "Loaded ${messages.size} messages")
        Log.d("Firebase", "First doc: ${snapshot.documents.firstOrNull()?.id} (${snapshot.documents.firstOrNull()?.get("timestamp")})")
        Log.d("Firebase", "Last doc: ${snapshot.documents.lastOrNull()?.id} (${snapshot.documents.lastOrNull()?.get("timestamp")})")
        Log.d("Firebase", "New cursor: ${newCursor?.id} (${newCursor?.get("timestamp")})")


        return messages to newCursor
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