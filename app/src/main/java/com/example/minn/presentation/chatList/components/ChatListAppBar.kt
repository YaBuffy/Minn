package com.example.minn.presentation.chatList.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.minn.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListAppBar(
    onMenu: ()->Unit,
    onSearch: ()->Unit,
){
    TopAppBar(
        title = {Text(stringResource(R.string.chat_list))},
        navigationIcon = {
            IconButton(
                onClick = {onMenu()},
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_menu_24),
                    contentDescription = null)
            }
        },
        actions = {
            IconButton(
                onClick = {onSearch()},
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_search_24),
                    contentDescription = null
                )
            }
        }
    )
}