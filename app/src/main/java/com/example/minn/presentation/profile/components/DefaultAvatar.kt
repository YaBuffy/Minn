package com.example.minn.presentation.profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minn.R
import com.example.minn.Util.imageToBase64
import kotlin.random.Random

@Composable
fun DefaultAvatar(
    name: String,
    size: Dp = 128.dp
){
    val randomColor = remember { mutableStateOf(Color.hsl(
        hue = Random.nextFloat() * 360f, // любой оттенок
        saturation = 0.7f,               // высокая насыщенность = ярко
        lightness = 0.6f                 // не тёмный и не белый
    )) }
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape = RoundedCornerShape(100))
            .background(color = randomColor.value, shape = RoundedCornerShape(100)),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = if (name == "") "A"
            else name.first().toString().uppercase(),
            fontSize = if (size == 128.dp) 40.sp else 20.sp
        )
    }
}

@Composable
fun EditDefaultAvatar(
    name: String,
    onAvatarSelected: (String)-> Unit
){
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val base64 = imageToBase64(it, contentResolver)
                onAvatarSelected(base64)
            }
        }

    val randomColor = remember { mutableStateOf(Color.hsl(
        hue = Random.nextFloat() * 360f, // любой оттенок
        saturation = 0.7f,               // высокая насыщенность = ярко
        lightness = 0.6f                 // не тёмный и не белый
    )) }
    Box(
        contentAlignment = Alignment.TopEnd
    ){
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(shape = RoundedCornerShape(100))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(100)
                ),
            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(shape = RoundedCornerShape(100))
                    .background(color = randomColor.value, shape = RoundedCornerShape(100))
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = if (name == "") "A"
                    else name.first().toString().uppercase(),
                    fontSize = 40.sp,
                    color = Color.Black
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(end = 5.dp, top = 5.dp)
                .size(32.dp)
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(R.drawable.outline_edit_24),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary,
            )

        }
    }
}