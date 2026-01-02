package com.example.minn.presentation.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.minn.R

@Composable
fun ButtonSign(
    signInOrSignUp: String,
    onClick: ()->Unit
){
    Button(
        onClick = {onClick()},
        modifier = Modifier
            .fillMaxWidth(0.75f),
        shape = MaterialTheme.shapes.small,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = signInOrSignUp,
            modifier = Modifier.padding(8.dp)
        )
    }
}