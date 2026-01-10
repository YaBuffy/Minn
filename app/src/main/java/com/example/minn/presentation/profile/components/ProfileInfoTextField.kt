package com.example.minn.presentation.profile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileInfoTextField(
    text: String,
    label: String,
    onValueChange: (String)->Unit,
){
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(start = 10.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(0.9f),
        maxLines = 3,
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimaryContainer),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.4f)
                )
            }
            innerTextField()
        }
    )
}