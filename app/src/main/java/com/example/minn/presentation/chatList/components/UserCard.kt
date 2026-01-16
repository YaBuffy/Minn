package com.example.minn.presentation.chatList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.minn.R
import com.example.minn.domain.model.User
import com.example.minn.presentation.profile.components.Avatar
import com.example.minn.presentation.profile.components.DefaultAvatar

@Composable
fun UserCard(
    user: User,
    onChat: (String)->Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable {onChat(user.uid) },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Avatar
                if (user.avatar.isNotEmpty()) {
                    Avatar(user.avatar)
                } else {
                    DefaultAvatar(user.name)
                }

                Spacer(modifier = Modifier.width(12.dp))

                // User info
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Name + Surname
                    Text(
                        text = "${user.name} ${user.surname}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    // Gender
                    Text(
                        text = "${stringResource(R.string.gender)}: ${stringResource(user.gender.labelRes)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Bio
                    if (user.bio.isNotBlank()) {
                        Text(
                            text = user.bio,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
            }
        }
    }
}
