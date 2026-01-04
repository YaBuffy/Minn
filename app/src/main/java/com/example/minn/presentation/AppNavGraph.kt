package com.example.minn.presentation

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.minn.Screen
import com.example.minn.presentation.auth.SignInScreen
import com.example.minn.presentation.auth.SignUpScreen
import com.example.minn.presentation.chat.ChatScreen
import com.example.minn.presentation.profile.ProfileScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isAuth: Boolean
){
    NavHost(
        navController = navController,
        startDestination = if(isAuth) Screen.Chat.route else Screen.SignIn.route
    ){
        composable(Screen.SignIn.route) {
            SignInScreen(
                onSignUpScreen = { navController.navigate(Screen.SignUp.route) },
                onChatScreen = {
                    navController.navigate(Screen.Profile.route) {
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
                onChatScreen = {navController.navigate(Screen.Profile.route)}
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onSignInScreen = {navController.navigate(Screen.SignIn.route) })}
    }
}