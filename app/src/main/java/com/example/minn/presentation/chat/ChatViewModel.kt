package com.example.minn.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.domain.model.Message
import com.example.minn.domain.usecase.chatUseCase.GetMessageUseCase
import com.example.minn.domain.usecase.chatUseCase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor (
    private val getMessageUseCase: GetMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase
): ViewModel() {
    fun getMessage(chatId: String): StateFlow<List<Message>> =
        getMessageUseCase(chatId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun sendMessage(text: String, chatId: String){
        viewModelScope.launch {
            sendMessageUseCase(chatId = chatId, text = text)
        }
    }
}