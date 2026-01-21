package com.example.minn.presentation.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.minn.R
import com.example.minn.presentation.chat.OpponentUIState
import com.example.minn.presentation.profile.components.Avatar
import com.example.minn.presentation.profile.components.DefaultAvatar
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppBar(
    onBack: ()->Unit,
    opponent: OpponentUIState,
    formatLastSeen: (Timestamp?)-> String
){
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(opponent.avatar.isNotBlank()){
                    Avatar(opponent.avatar, size = 48.dp)
                } else{
                    DefaultAvatar(opponent.name, size = 48.dp)
                }
                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = opponent.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = if (opponent.online) stringResource(R.string.online)
                            else formatLastSeen(opponent.lastSeen),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = if (opponent.online)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
                },
        navigationIcon = {
            IconButton(
                onClick = {onBack()},
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null)
            }
        },
        actions = {
            IconButton(
                onClick = {},
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_more_vert_24),
                    contentDescription = null
                )
            }
        }
    )
}