package com.example.minn.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.minn.presentation.auth.AuthViewModel

@Composable
fun RootScreen(
    vm: AuthViewModel = hiltViewModel()
){
    val state by vm.state.collectAsState()
    val navController = rememberNavController()

    when{
        state.isLoading -> {
            CircularProgressIndicator()
        }
        else -> AppNavGraph(navController, isAuth = state.isAuthorized)
    }
}