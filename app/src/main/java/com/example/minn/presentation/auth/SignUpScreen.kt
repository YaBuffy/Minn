package com.example.minn.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.R
import com.example.minn.presentation.auth.components.PrimaryButton
import com.example.minn.presentation.auth.components.EmailTextField
import com.example.minn.presentation.auth.components.NameTextField
import com.example.minn.presentation.auth.components.PasswordTextField


@Composable
fun SignUpScreen(
    vm: AuthViewModel = hiltViewModel(),
    onBack: ()-> Unit,
    onChatScreen: ()-> Unit
){
    val email = vm.email
    val name = vm.name
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
        Row(
            modifier = Modifier
                .systemBarsPadding()
                .padding(top = 5.dp)
                .fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.Start
            
        ){
            IconButton(
                onClick = {onBack()}
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Back")
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "logo",
            modifier = Modifier
                .padding(top = 80.dp)
        )
        Text(
            text = stringResource(R.string.create_your_account),
            fontSize = 18.sp,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(top = 30.dp, bottom = 20.dp)
        )

        NameTextField(
            entry = name,
        ){
            vm.onNameChange(it)
        }

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

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

        PrimaryButton(
            buttonText = stringResource(R.string.sign_up),
            onClick = {vm.signUp(email,password, name)}
        )



    }
}