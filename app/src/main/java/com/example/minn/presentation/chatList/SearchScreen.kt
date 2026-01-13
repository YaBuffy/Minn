package com.example.minn.presentation.chatList

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.presentation.chatList.components.UserCard
import com.example.minn.presentation.chatList.components.UserListRow
import com.example.minn.presentation.chatList.components.UserSearchBar

@Composable
fun SearchScreen(
    vm: ChatListViewModel = hiltViewModel(),
    onBack: ()->Unit
){
    val state by vm.state.collectAsState()
    val users by vm.users.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            UserSearchBar(
                searchQuery = searchQuery,
                onQueryChange = {vm.onSearchChange(it)},
                onBack = onBack,
                onSearchClear = {vm.onSearchClear()}
            )
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            if (searchQuery==""){
                item{
                    UserListRow(
                        users = state.users
                    )
                }
            } else{
                items(users){user->
                    UserCard(user)
                }
            }
        }
    }
}