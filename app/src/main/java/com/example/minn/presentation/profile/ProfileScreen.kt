package com.example.minn.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    vm: ProfileViewModel = hiltViewModel(),
    onSignInScreen: ()->Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("profile")
        Button(
            onClick = {vm.signOut(); onSignInScreen()}
        ) { Text(
            "Sign Out"
        ) }

    }
}
