package com.example.minn.presentation.chat

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatScreen(
    chatId: String,
    onBack: ()-> Unit,
    vm: ChatViewModel = hiltViewModel()
){
    LazyColumn(
        modifier = Modifier.systemBarsPadding(),
    ) {
        item{
            Text("Это чат")
        }
    }
}