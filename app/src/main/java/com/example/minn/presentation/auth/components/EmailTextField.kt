package com.example.minn.presentation.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.minn.R

@Composable
fun EmailTextField(
    entry: String,
    isError: Boolean,
    onChange: (String)-> Unit = {}
){

    OutlinedTextField(
        value = entry,
        maxLines = 1,
        onValueChange = onChange,
        isError = isError,
        label = {
            Text(
                text = stringResource(R.string.email),
                color = MaterialTheme.colorScheme.onSurfaceVariant
        ) },
        leadingIcon = { Icon(painter = painterResource(R.drawable.baseline_email_24), contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier
            .fillMaxWidth(0.75f)
    )
}