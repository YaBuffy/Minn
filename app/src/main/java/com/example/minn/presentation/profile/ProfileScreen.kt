package com.example.minn.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minn.R
import com.example.minn.presentation.auth.components.PrimaryButton
import com.example.minn.presentation.profile.components.Avatar
import com.example.minn.presentation.profile.components.BirthDatePicker
import com.example.minn.presentation.profile.components.DefaultAvatar
import com.example.minn.presentation.profile.components.GenderDropDown
import com.example.minn.presentation.profile.components.ProfileAppBar
import com.example.minn.presentation.profile.components.ProfileInfoText

@Composable
fun ProfileScreen(
    vm: ProfileViewModel = hiltViewModel(),
){
    val state by vm.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var name by rememberSaveable { mutableStateOf(state.name)  }

    LaunchedEffect(state.name) {
        if (name.isEmpty()) {
            name = state.name
        }
    }

    val focusManager = LocalFocusManager.current

    val profileUpdatedText = stringResource(R.string.profile_updated)
    LaunchedEffect(state.success) {
        if(state.success){
            snackbarHostState.showSnackbar(message = profileUpdatedText)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {ProfileAppBar(
            onBack = {},
            onSignOut = {vm.signOut()}
        )}
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
                Avatar(state.avatar)
            } else{
                DefaultAvatar(state.name)
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = name,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = state.email,
                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileInfoText(
                title = stringResource(R.string.name),
                icon = R.drawable.outline_person_24,
                content = state.name
            ){
                vm.onNameChange(it)
            }

            Spacer(modifier = Modifier.height(10.dp))

            ProfileInfoText(
                title = stringResource(R.string.surname),
                icon = R.drawable.outline_id_card_24,
                content = state.surname
            ){
                vm.onSurnameChange(it)
            }

            Spacer(modifier = Modifier.height(10.dp))

            ProfileInfoText(
                title = stringResource(R.string.bio),
                icon = R.drawable.outline_email_24,
                content = state.bio
            ){
                vm.onBioChange(it)
            }

            Spacer(modifier = Modifier.height(10.dp))

//            ProfileInfoText(
//                title = stringResource(R.string.gender),
//                icon = R.drawable.outline_wc_24,
//                content = stringResource(state.gender.labelRes)
//            ){
//            }
            GenderDropDown(
                selectedGender = state.gender,
                onGenderSelected = {
                    vm.onGenderChange(it)
                }
            )
            
            Spacer(modifier = Modifier.height(10.dp))

            BirthDatePicker(
                birthDate = state.birthDate
            ) {
                vm.onBirthDateChange(it)
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                buttonText = stringResource(R.string.edit_profile),
                isBright = false
            ) {
                vm.updateUser()
            }

            Spacer(modifier = Modifier.height(20.dp))

        }

    }
}
