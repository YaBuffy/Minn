package com.example.minn.presentation.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.minn.domain.model.Message

@Composable
fun MessageCard(
    isMine: Boolean,
    message: Message,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, top = 5.dp, start = 5.dp, end = 5.dp),
        horizontalArrangement = if(isMine) Arrangement.End else Arrangement.Start
    ){
        Card(
            modifier = Modifier
                .widthIn(max = 280.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = if (isMine) {
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            }
        ){
            Box(
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
            ){
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge)

            }
        }
    }
}