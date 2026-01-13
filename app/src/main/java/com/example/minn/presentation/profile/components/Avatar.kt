package com.example.minn.presentation.profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.minn.R
import com.example.minn.Util.base64ToBitmap
import com.example.minn.Util.imageToBase64

@Composable
fun Avatar(
    imageUrl: String,
    size: Dp = 128.dp
){
    val bitmap = base64ToBitmap(imageUrl)

    Image(
        bitmap = bitmap.asImageBitmap() ,
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .clip(shape = RoundedCornerShape(100)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun EditAvatar(
    imageUrl: String,
    onAvatarSelected: (String)-> Unit
) {
    val bitmap = base64ToBitmap(imageUrl)
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
            Image(
                bitmap = bitmap.asImageBitmap() ,
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .clip(shape = RoundedCornerShape(100))
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    },
                contentScale = ContentScale.Crop
            )
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

