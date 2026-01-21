package com.example.minn.presentation.chat

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.presentation.chat.components.ChatAppBar
import com.example.minn.presentation.chat.components.ChatTextField
import com.example.minn.presentation.chat.components.MessageCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    chatId: String,
    opponentUid: String,
    onBack: ()-> Unit,
    vm: ChatViewModel = hiltViewModel()
){
    val chatText by vm.chatText.collectAsState()
    val messages by vm.messages.collectAsState()
    val opponent by vm.opponentState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(chatId) {
        vm.setChatId(chatId)
    }
    LaunchedEffect(opponentUid) {
        vm.loadOpponent(opponentUid)
    }

    Scaffold(
        topBar = {ChatAppBar(
            onBack = onBack,
            opponent = opponent,
            formatLastSeen = {vm.formatLastSeen(it)}
        )},
        contentWindowInsets = WindowInsets.systemBars,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            }
    ) {paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(
                    items = messages.reversed(),
                    key = { it.id }
                ) { message ->
                    MessageCard(
                        isMine = message.senderId == vm.currentUserId,
                        message = message
                    )
                }
            }

            ChatTextField(
                text = chatText,
                onChange = {vm.onChatChange(it)},
                sendMessage = { text -> vm.sendMessage(text, chatId) },
            )
        }

    }
}