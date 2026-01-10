package com.example.minn

sealed class Screen(val route: String) {
    object SignIn: Screen("sign_in")
    object SignUp: Screen("sign_up")
    object Chat: Screen("chat")
    object Profile: Screen("profile")
    object EditProfile: Screen("edit_profile")
}