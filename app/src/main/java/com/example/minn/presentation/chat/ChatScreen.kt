package com.example.minn.presentation.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.presentation.chat.components.ChatAppBar
import com.example.minn.presentation.chat.components.ChatTextField
import com.example.minn.presentation.chat.components.MessageCard

@SuppressLint("FrequentlyChangingValue")
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
    val listState = rememberLazyListState()
    var wasAtBottom by remember { mutableStateOf(true) }

    LaunchedEffect(chatId) {
        vm.startChat(chatId)
    }
    LaunchedEffect(opponentUid) {
        vm.loadOpponent(opponentUid)
    }

    //scroll only if user at bottom
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                wasAtBottom = index == 0
            }
    }
    LaunchedEffect(messages.firstOrNull()?.id) {
        if (messages.isNotEmpty() && wasAtBottom) {
            listState.animateScrollToItem(0)
        }
    }
    LaunchedEffect(messages.firstOrNull()?.id) {
        val newest = messages.firstOrNull() ?: return@LaunchedEffect

        if (newest.senderId == vm.currentUserId || listState.isAtBottom()) {
            listState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        // когда прокручиваем до верхушки (oldest messages)
        if (listState.firstVisibleItemIndex >= messages.lastIndex && !listState.isScrollInProgress) {
            Log.d("message", "loadOlderMes")
            vm.loadOlder()
        }
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isEmpty()) return@collect

                val firstVisible = visibleItems.first().index
                val lastVisible = visibleItems.last().index
                val totalItems = messages.size

                Log.d("Pagination", "First: $firstVisible, Last: $lastVisible, Total: $totalItems")

                // При reverseLayout = true, скролл вверх увеличивает индекс
                if (lastVisible >= totalItems - 3) {  // триггер за 3 элемента до конца
                    Log.d("Pagination", "Triggering loadOlder()")
                    vm.loadOlder()
                }
            }
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
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(
                    items = messages,
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
fun LazyListState.isAtBottom(): Boolean {
    return firstVisibleItemIndex == 0
}
