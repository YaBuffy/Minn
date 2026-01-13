package com.example.minn.presentation.chatList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.minn.domain.model.User
import com.example.minn.presentation.profile.components.Avatar
import com.example.minn.presentation.profile.components.DefaultAvatar

@Composable
fun UserListRow(
    users: List<User>
){
    LazyRow() {
        items(users){user->
            Column(modifier = Modifier
                .padding(start = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (user.avatar != ""){
                    Avatar(
                        imageUrl = user.avatar,
                        size = 64.dp
                    )
                } else {
                    DefaultAvatar(
                        name = user.name,
                        size = 64.dp
                    )
                }

                Text(user.name)
            }
        }
    }
}