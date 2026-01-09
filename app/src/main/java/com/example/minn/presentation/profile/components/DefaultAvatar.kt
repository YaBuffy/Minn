package com.example.minn.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun DefaultAvatar(name: String){
    val randomColor = Color.hsl(
        hue = Random.nextFloat() * 360f, // любой оттенок
        saturation = 0.7f,               // высокая насыщенность = ярко
        lightness = 0.6f                 // не тёмный и не белый
    )
    Box(
        modifier = Modifier
            .size(128.dp)
            .clip(shape = RoundedCornerShape(100))
            .background(color = randomColor, shape = RoundedCornerShape(100)),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = if (name == "") "A"
            else name.first().toString().uppercase(),
            fontSize = 40.sp
        )
    }
}

@Preview
@Composable
fun prevDA(){
    val name = "ASd"
    DefaultAvatar(name)
}