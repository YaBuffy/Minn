package com.example.minn.presentation.profile.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.minn.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileAppBar(
    onBack: ()->Unit,
    onSave: ()->Unit,
    isLoading: Boolean,
){
    TopAppBar(
        title = {Text(stringResource(R.string.edit_profile))},
        navigationIcon = {
            IconButton(
                onClick = {onBack()},
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null)
            }
        },
        actions = {
            IconButton(
                onClick = {onSave()},
                enabled = !isLoading
            ) {
                if (isLoading){
                    CircularProgressIndicator()
                }else{
                    Icon(
                        painter = painterResource(R.drawable.outline_check_24),
                        contentDescription = null
                    )
                }
            }
        }
    )
}