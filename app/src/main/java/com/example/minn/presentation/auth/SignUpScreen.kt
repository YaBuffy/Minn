package com.example.minn.presentation.auth

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.R
import com.example.minn.presentation.auth.components.ButtonSign
import com.example.minn.presentation.auth.components.EmailTextField
import com.example.minn.presentation.auth.components.PasswordTextField


@Composable
fun SignUpScreen(
    vm: AuthViewModel = hiltViewModel()
){
    val email = vm.email
    val password = vm.password
    val focusManager = LocalFocusManager.current

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
                .padding(top = 100.dp)
        )
        Text(
            text = "Create your Account",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
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
            signInOrSignUp = "Sign Up",
            onClick = {vm.signUp(email,password)}
        )



    }
}