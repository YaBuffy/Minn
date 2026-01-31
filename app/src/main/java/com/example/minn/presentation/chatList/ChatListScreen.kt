package com.example.minn.presentation.chatList

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.presentation.chatList.components.ChatCard
import com.example.minn.presentation.chatList.components.ChatListAppBar
import com.example.minn.presentation.chatList.components.UserListRow

@Composable
fun ChatListScreen(
    vm: ChatListViewModel = hiltViewModel(),
    onProfile: ()-> Unit,
    onSearch: ()-> Unit,
    onChat: (String, String)-> Unit
){

    val state by vm.state.collectAsState()
    val chat by vm.chats.collectAsState()
    val opponents by vm.opponents.collectAsState()

    LaunchedEffect(Unit) {
        vm.openChat.collect { event ->
            Log.d("ChatRepo", "chatId in openChat is ${event.chatId}")
            onChat(event.chatId, event.opponentUid)
        }
    }

    LaunchedEffect(Unit) {
        vm.loadLastChats()
    }

    Scaffold(
        topBar = { ChatListAppBar(
            onMenu = onProfile,
            onSearch = onSearch
        )},
    ) {paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)) {
            UserListRow(
                users = state.users,
                onChat = {vm.openChat(it)}
            )
            LazyColumn() {
                items(chat){chat->
                    if (chat.lastMessage!=""){
                        LaunchedEffect(chat.id) {
                            vm.loadOpponent(chat)
                        }
                        val opponentId = chat.members.first{it!=vm.currentUser}
                        val opponent = opponents[opponentId]
                        ChatCard(
                            chat = chat,
                            opponentName = opponent?.name ?: "Loading...",
                            avatar = opponent?.avatar ?: ""
                        ) {
                            vm.openChat(opponentId)
                        }
                    }


                }
            }
        }

    }

}