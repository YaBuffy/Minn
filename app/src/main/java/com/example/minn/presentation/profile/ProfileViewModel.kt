package com.example.minn.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.Util.Response
import com.example.minn.domain.usecase.authUseCase.AuthUseCases
import com.example.minn.domain.usecase.authUseCase.SignOutUseCase
import com.example.minn.domain.usecase.userUseCase.UpdateUserUseCase
import com.example.minn.presentation.auth.AuthUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val updateUserUseCase: UpdateUserUseCase
): ViewModel(){

    fun signOut(){
        viewModelScope.launch {
            signOutUseCase().collect()
        }
    }
}