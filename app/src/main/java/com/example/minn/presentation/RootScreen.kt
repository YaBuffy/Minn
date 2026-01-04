package com.example.minn.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.minn.Screen
import com.example.minn.presentation.auth.AuthViewModel

@Composable
fun RootScreen(
    vm: AuthViewModel = hiltViewModel()
){
    val state by vm.state.collectAsState()
    val navController = rememberNavController()

    LaunchedEffect(state.isAuthorized) {
//        delay(1000)
        if(!state.isAuthorized){
            navController.navigate(Screen.SignIn.route){
                popUpTo(0){inclusive = true}
            }
        }
    }
    AppNavGraph(navController, isAuth = state.isAuthorized)
}