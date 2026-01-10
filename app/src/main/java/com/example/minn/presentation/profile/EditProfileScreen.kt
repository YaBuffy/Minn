package com.example.minn.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.R
import com.example.minn.presentation.profile.components.BirthDatePicker
import com.example.minn.presentation.profile.components.EditAvatar
import com.example.minn.presentation.profile.components.EditDefaultAvatar
import com.example.minn.presentation.profile.components.EditProfileAppBar
import com.example.minn.presentation.profile.components.GenderDropDown
import com.example.minn.presentation.profile.components.ProfileInfoTextField

@Composable
fun EditProfileScreen(
    vm: ProfileViewModel = hiltViewModel(),
    onBack: ()-> Unit
){
    val focusManager = LocalFocusManager.current
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { EditProfileAppBar(
            onBack = {onBack()},
            isLoading = state.isLoading,
            onSave = {
                vm.updateUser()
                onBack()
            }
        ) }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit){
                    detectTapGestures (onTap = {
                        focusManager.clearFocus()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(state.avatar!= ""){
                EditAvatar(
                    imageUrl = state.avatar,
                    onAvatarSelected = {vm.onAvatarChange(it)}
                    )
            } else{
                EditDefaultAvatar(
                    state.name,
                    onAvatarSelected = {vm.onAvatarChange(it)})
            }

            Text(
                text = stringResource(R.string.about_me),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(0.9f)
            )

            ProfileInfoTextField(
                text = state.name,
                label = stringResource(R.string.name_low),
                onValueChange = {vm.onNameChange(it)}
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 5.dp)
            )
            ProfileInfoTextField(
                text = state.surname,
                label = stringResource(R.string.surname),
                onValueChange = {vm.onSurnameChange(it)}
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 5.dp)
            )
            ProfileInfoTextField(
                text = state.bio,
                label = stringResource(R.string.bio),
                onValueChange = {vm.onBioChange(it)}
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 5.dp)
            )
            GenderDropDown(
                selectedGender = state.gender,
            ) {
                vm.onGenderChange(it)
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 5.dp)
            )
            BirthDatePicker(
                birthDate = state.birthDate,
            ) {
                vm.onBirthDateChange(it)
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            )
        }
    }
}