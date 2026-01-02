package com.example.minn.presentation.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordTextField(
    entry: String,
    onChange: (String)-> Unit = {}
){
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = entry,
        onValueChange = onChange,
        label = {Text(text = "password")},
        leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null) },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.75f),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if(passwordVisibility) Icons.Default.Visibility
            else Icons.Default.VisibilityOff
            IconButton(
                onClick = {passwordVisibility=!passwordVisibility}
            ) {
                Icon(
                    imageVector = image,
                    contentDescription = "password visibility"
                )
            }
        }
    )
}