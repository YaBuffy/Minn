package com.example.minn.presentation.chat

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.R
import com.example.minn.Util.Response
import com.example.minn.domain.model.Message
import com.example.minn.domain.usecase.chatUseCase.GetMessageUseCase
import com.example.minn.domain.usecase.chatUseCase.SendMessageUseCase
import com.example.minn.domain.usecase.userUseCase.GetUserUseCase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Time
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor (
    private val getMessageUseCase: GetMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val resources: Resources,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _chatText = MutableStateFlow("")
    val chatText = _chatText.asStateFlow()
    private val _chatId = MutableStateFlow<String?>(null)

    val currentUserId = auth.currentUser?.uid
    private val _opponentState = MutableStateFlow(OpponentUIState())
    val opponentState = _opponentState.asStateFlow()

    fun setChatId(chatId: String) {
        _chatId.value = chatId
    }

    fun onChatChange(newText: String){
        _chatText.value = newText
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val messages: StateFlow<List<Message>> =
        _chatId
            .filterNotNull()
            .flatMapLatest { chatId ->
                getMessageUseCase(chatId)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun sendMessage(text: String, chatId: String){
        viewModelScope.launch {
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