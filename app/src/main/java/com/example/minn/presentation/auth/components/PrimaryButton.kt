package com.example.minn.presentation.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    buttonText: String,
    isBright: Boolean = true,
    onClick: ()->Unit
){
    val buttonColor = if (isBright) ButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.secondary,
        disabledContentColor = MaterialTheme.colorScheme.onPrimary
    ) else ButtonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )

    Button(
        onClick = {onClick()},
        modifier = Modifier
            .fillMaxWidth(0.75f),
        shape = MaterialTheme.shapes.small,
        colors = buttonColor
    ) {
        Text(
            text = buttonText,
            modifier = Modifier.padding(8.dp)
        )
    }
}