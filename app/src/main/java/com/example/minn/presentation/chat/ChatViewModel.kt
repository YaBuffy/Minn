package com.example.minn.presentation.chat

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.R
import com.example.minn.Util.Response
import com.example.minn.domain.model.Message
import com.example.minn.domain.usecase.chatUseCase.LoadLastMessagesUseCase
import com.example.minn.domain.usecase.chatUseCase.LoadOlderMessagesUseCase
import com.example.minn.domain.usecase.chatUseCase.ObserveNewMessagesUseCase
import com.example.minn.domain.usecase.chatUseCase.SendMessageUseCase
import com.example.minn.domain.usecase.userUseCase.GetUserUseCase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor (
    private val observeNewMessagesUseCase: ObserveNewMessagesUseCase,
    private val loadOlderMessagesUseCase: LoadOlderMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val loadLastMessagesUseCase: LoadLastMessagesUseCase,
    private val resources: Resources,
    auth: FirebaseAuth
): ViewModel() {

    private val _chatText = MutableStateFlow("")
    val chatText = _chatText.asStateFlow()
    private val _chatId = MutableStateFlow("")

    val currentUserId = auth.currentUser?.uid
    private val _opponentState = MutableStateFlow(OpponentUIState())
    val opponentState = _opponentState.asStateFlow()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private var lastMessageSnapshot: DocumentSnapshot? = null
    private var isLoading = false
    private var observerJob: Job? = null

    fun onChatChange(newText: String){
        _chatText.value = newText
    }

    fun startChat(chatId: String) {
        if (_chatId.value == chatId) return

        _chatId.value = chatId
        observerJob?.cancel()

        _messages.value = emptyList()
        lastMessageSnapshot = null
        isLoading = false

        viewModelScope.launch {
            val (initial, cursor) = loadLastMessagesUseCase(chatId)
            _messages.value = initial
            lastMessageSnapshot = cursor
            Log.d("messages", "loadLastMessagesUseCase")

            observerJob = launch {
                observeNewMessagesUseCase(chatId).collect { new ->
                    if (new.isEmpty()) return@collect

                    val filtered = new.filterNot { incoming ->
                        _messages.value.any { it.id == incoming.id }
                    }

                    if (filtered.isNotEmpty()) {
                        _messages.value = filtered + _messages.value
                    }
                }
            }
        }
    }
//    private fun observerMessages(chatId: String) {
//        observerJob = viewModelScope.launch {
//            observeNewMessagesUseCase(chatId).collect { incoming ->
//                _messages.value = mergeMessages(_messages.value, incoming)
//            }
//        }
//    }
    fun loadOlder() {
        Log.d("Pagination", "loadOlder CALLED - isLoading: $isLoading, hasSnapshot: ${lastMessageSnapshot != null}")
        if (isLoading || lastMessageSnapshot == null) return
        isLoading = true

        viewModelScope.launch {
            Log.d("Pagination", "Current cursor timestamp: ${lastMessageSnapshot?.get("timestamp")}")
            val (older, newCursor) =
                loadOlderMessagesUseCase(_chatId.value, lastMessageSnapshot!!)
            if (older.isEmpty()) {
                Log.d("Pagination", "No more messages!")
                isLoading = false
                lastMessageSnapshot = null
                return@launch
            }

            val uniqueOlder = older.filterNot { newMsg ->
                _messages.value.any { it.id == newMsg.id }
            }

            lastMessageSnapshot = newCursor

            if (uniqueOlder.isNotEmpty()) {
                _messages.value += uniqueOlder
            } else {
                Log.d("Pagination", "All duplicates! Stopping.")
            }

            isLoading = false
        }
    }

//    fun observerMessages(){
//        viewModelScope.launch {
//            observeNewMessagesUseCase(_chatId.value).collect {snapshot ->
//                val newMessages = snapshot.toObjects(Message::class.java)
//                _messages.value = newMessages + _messages.value
//                lastMessageSnapshot = snapshot.documents.lastOrNull()
//            }
//        }
//    }

//    fun loadOlderMessages(){
//        if (isLoadingPage) return
//        isLoadingPage = true
//        viewModelScope.launch {
//            val (older,newCursor) = loadOlderMessagesUseCase(_chatId.value, lastMessageSnapshot)
//            Log.d("loadOlderMessages", lastMessageSnapshot.toString())
//            if (older.isNotEmpty()){
//                _messages.value +=older
//                Log.d("message", _messages.value.last().text)
//                lastMessageSnapshot = newCursor
//            }
//            isLoadingPage = false
//        }
//    }

    fun sendMessage(text: String, chatId: String){
        viewModelScope.launch {
            Log.d("ChatRepo", "chatId is ${chatId}")
            sendMessageUseCase(chatId = chatId, text = text)
        }
        _chatText.value = ""
    }

    fun loadOpponent(opponentUid: String) {

        viewModelScope.launch {
            getUserUseCase(opponentUid).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        _opponentState.value = _opponentState.value.copy(isLoading = true)
                    }
                    is Response.Success -> {
                        Log.d("Chat", "isOnline from Firestore: ${response.data.online}")
                        Log.d("Chat", "lastSeen from Firestore: ${response.data.lastSeen}")
                        _opponentState.value = _opponentState.value.copy(
                            isLoading = false,
                            name = response.data.name,
                            avatar = response.data.avatar,
                            online = response.data.online,
                            lastSeen = response.data.lastSeen
                        )
                    }
                    is Response.Error -> {
                        _opponentState.value = _opponentState.value.copy(
                            isLoading = false,
                            error = response.message
                        )
                    }
                }
            }
        }
    }
    fun formatLastSeen(timestamp: Timestamp?): String {
        val dt = timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
        val now = LocalDateTime.now()
        val diff = dt?.let { Duration.between(it, now) }

        return when {
            diff == null -> resources.getString(R.string.offline)
            diff.toMinutes() < 1 -> resources.getString(R.string.last_seen_just_now)
            diff.toHours() < 1 -> resources.getString(R.string.last_seen_minutes_ago, diff.toMinutes())
            diff.toHours() < 24 -> resources.getString(R.string.last_seen_hours_ago, diff.toHours())
            else -> resources.getString(R.string.last_seen_on, dt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
        }
    }
}

data class OpponentUIState(
    val name: String = "",
    val avatar: String = "",
    val online: Boolean = false,
    val lastSeen: Timestamp? = null,
    val isLoading: Boolean = true,
    val success: Boolean = false,
    val error: String? = null
)