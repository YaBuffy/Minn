package com.example.minn.presentation.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.minn.R

@Composable
fun PasswordTextField(
    entry: String,
    isError: Boolean,
    onChange: (String)-> Unit = {}
){
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = entry,
        maxLines = 1,
        isError = isError,
        onValueChange = onChange,
        label = {
            Text(
                text = stringResource(R.string.password),
                color = MaterialTheme.colorScheme.onSurfaceVariant
        ) },
        leadingIcon = { Icon(painter = painterResource(R.drawable.outline_password_2_24), contentDescription = null) },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.75f),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if(passwordVisibility) painterResource(R.drawable.baseline_visibility_24)
            else painterResource(R.drawable.baseline_visibility_off_24)
            IconButton(
                onClick = {passwordVisibility=!passwordVisibility}
            ) {
                Icon(
                    painter = image,
                    contentDescription = "password visibility"
                )
            }
        }
    )
}