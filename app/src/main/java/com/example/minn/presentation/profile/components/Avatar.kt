package com.example.minn.presentation.profile.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Avatar(
    imageUrl: String
){
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .size(128.dp)
            .clip(shape = RoundedCornerShape(100))
    )
}
