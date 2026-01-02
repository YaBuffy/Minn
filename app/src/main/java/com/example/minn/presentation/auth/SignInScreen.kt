package com.example.minn.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.R
import com.example.minn.presentation.auth.components.ButtonSign
import com.example.minn.presentation.auth.components.EmailTextField
import com.example.minn.presentation.auth.components.PasswordTextField

@Composable
fun SignInScreen(
    vm: AuthViewModel = hiltViewModel(),
    onSignUpScreen: ()-> Unit,
    onChatScreen: ()->Unit

){
    val email = vm.email
    val password = vm.password
    val focusManager = LocalFocusManager.current
    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = state.isAuthorized) {
        if(state.isAuthorized){
            onChatScreen()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .pointerInput(Unit){
                detectTapGestures (onTap = {
                    focusManager.clearFocus()
                })

            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "logo",
            modifier = Modifier
                .systemBarsPadding()
                .padding(top = 120.dp)
        )
        Text(
            text = "Login to your Account",
            fontSize = 18.sp,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(top = 30.dp, bottom = 20.dp)
        )

        EmailTextField(
            entry = email,
        ){
            vm.onEmailChange(it)
        }

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        PasswordTextField(
            entry = password
        ){
            vm.onPasswordChange(it)
        }

        Spacer(modifier = Modifier.padding(vertical = 15.dp))

        ButtonSign(
            signInOrSignUp = "Sign In",
            onClick = {vm.signIn(email,password)}
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
            Text(
                text = "Sign Up",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable{onSignUpScreen()}
            )
        }


    }
}