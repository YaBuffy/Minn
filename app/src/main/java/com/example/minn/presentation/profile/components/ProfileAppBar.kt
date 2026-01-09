package com.example.minn.presentation.profile.components

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
fun ProfileAppBar(
    onBack: ()-> Unit,
    onSignOut: ()->Unit
){
    TopAppBar(
        title = {Text(stringResource(R.string.profile))},
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
                onClick = {onSignOut()}
            ) {
                Icon(painter = painterResource(R.drawable.outline_exit_to_app_24),
                    contentDescription = null)
            }
        }
    )
}