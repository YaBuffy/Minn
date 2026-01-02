package com.example.minn.presentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.presentation.auth.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun ChatScreen(
    vm: AuthViewModel = hiltViewModel(),
    onSignInScreen: ()-> Unit
){
    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = state.isAuthorized) {
        delay(1000)
        if(!state.isAuthorized){
            onSignInScreen()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("chat list")
        Button(
            onClick = {vm.signOut()}
        ) { Text(
            "Sign Out"
        ) }

    }
}