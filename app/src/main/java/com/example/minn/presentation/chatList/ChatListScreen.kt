package com.example.minn.presentation.chatList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.presentation.chatList.components.ChatListAppBar
import com.example.minn.presentation.chatList.components.UserListRow

@Composable
fun ChatListScreen(
    vm: ChatListViewModel = hiltViewModel(),
    onProfile: ()-> Unit,
    onSearch: ()-> Unit,
    onChat: (String)-> Unit
){

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.openChat.collect { chatId ->
            onChat(chatId)
        }
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
            Button(
                onClick = {vm.signOut()}
            ) {
                Text("sign out")
            }
        }

    }

}