package com.example.minn.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minn.Screen
import com.example.minn.presentation.auth.SignInScreen
import com.example.minn.presentation.auth.SignUpScreen
import com.example.minn.presentation.chat.ChatScreen

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ){
        composable(Screen.SignIn.route) {
            SignInScreen(
                onSignUpScreen = { navController.navigate(Screen.SignUp.route) },
                onChatScreen = {
                    navController.navigate(Screen.Chat.route) {
                        popUpTo(Screen.SignIn.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onBack = {navController.navigate(Screen.SignIn.route)},
                onChatScreen = {navController.navigate(Screen.Chat.route)}
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen(
                onSignInScreen = {navController.navigate(Screen.SignIn.route)}
            )
        }
    }
}