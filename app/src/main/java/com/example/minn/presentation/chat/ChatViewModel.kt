package com.example.minn.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.domain.model.Message
import com.example.minn.domain.usecase.chatUseCase.GetMessageUseCase
import com.example.minn.domain.usecase.chatUseCase.SendMessageUseCase
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
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor (
    private val getMessageUseCase: GetMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _chatText = MutableStateFlow("")
    val chatText = _chatText.asStateFlow()
    private val _chatId = MutableStateFlow<String?>(null)

    val currentUserId = auth.currentUser?.uid

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
}