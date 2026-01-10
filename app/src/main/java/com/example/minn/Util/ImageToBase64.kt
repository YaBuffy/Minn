package com.example.minn.Util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64

fun imageToBase64(
    image: Uri,
    contentResolver: ContentResolver
): String{
    val inputStream = contentResolver.openInputStream(image)
        ?: throw IllegalArgumentException("Cannot open input stream")

    val bytes = inputStream.use { it.readBytes() }
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}
fun base64ToBitmap(base64: String): Bitmap {
    val bytes = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}