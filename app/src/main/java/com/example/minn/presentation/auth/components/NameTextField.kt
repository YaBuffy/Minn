package com.example.minn.presentation.auth.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.minn.R

@Composable
fun NameTextField(
    entry: String,
    isError: Boolean,
    onChange: (String)-> Unit = {}
){

    OutlinedTextField(
        value = entry,
        maxLines = 1,
        isError = isError,
        onValueChange = onChange,
        label = {
            Text(
                text = stringResource(R.string.name_low),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ) },
        leadingIcon = { Icon(painter = painterResource(R.drawable.baseline_person_24), contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth(0.75f)
    )
}