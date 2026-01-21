package com.example.minn.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.minn.Screen
import com.example.minn.presentation.auth.SignInScreen
import com.example.minn.presentation.auth.SignUpScreen
import com.example.minn.presentation.chat.ChatScreen
import com.example.minn.presentation.chatList.ChatListScreen
import com.example.minn.presentation.chatList.SearchScreen
import com.example.minn.presentation.profile.EditProfileScreen
import com.example.minn.presentation.profile.ProfileScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isAuth: Boolean
){
    NavHost(
        navController = navController,
        startDestination = if(isAuth) Screen.ChatList.route else Screen.SignIn.route
    ){
        composable(Screen.SignIn.route) {
            SignInScreen(
                onSignUpScreen = { navController.navigate(Screen.SignUp.route) },
                onChatScreen = {
                    navController.navigate(Screen.ChatList.route) {
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
                onChatScreen = {navController.navigate(Screen.ChatList.route)}
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBack = {navController.popBackStack()},
                onChat = {chatId, opponentUid ->
                    navController.navigate(Screen.Chat.createRoute(chatId, opponentUid))
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = {navController.popBackStack()},
                onEditProfile = {navController.navigate(Screen.EditProfile.route)}
            )
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onBack = {navController.popBackStack()}
            )
        }

        composable(Screen.ChatList.route) {
            ChatListScreen(
                onProfile = {navController.navigate(Screen.Profile.route)},
                onSearch = {navController.navigate(Screen.Search.route)},
                onChat = {chatId, opponentUid ->
                    navController.navigate(Screen.Chat.createRoute(chatId, opponentUid))}
            )
        }

        composable(Screen.Chat.route, arguments = listOf(
            navArgument("chatId"){
                type = NavType.StringType
            },
            navArgument("opponentUid"){
                type = NavType.StringType
            }
        )){backStackEntry->
            val chatId = backStackEntry
                .arguments
                ?.getString("chatId")
                ?: return@composable

            val opponentUid = backStackEntry
                .arguments
                ?.getString("opponentUid")
                ?: return@composable
            ChatScreen(
                chatId = chatId,
                opponentUid = opponentUid,
                onBack = {navController.popBackStack()}
            )

        }
    }
}