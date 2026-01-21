package com.example.minn

sealed class Screen(val route: String) {
    object SignIn: Screen("sign_in")
    object SignUp: Screen("sign_up")
    object ChatList: Screen("chat_list")
    object  Chat: Screen("chat/{chatId}/{opponentUid}"){
        fun createRoute(chatId: String, opponentUid: String): String = "chat/$chatId/$opponentUid"
    }
    object Search: Screen("search")
    object Profile: Screen("profile")
    object EditProfile: Screen("edit_profile")
}