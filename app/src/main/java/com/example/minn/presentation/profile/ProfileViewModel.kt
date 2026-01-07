package com.example.minn.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.Util.Response
import com.example.minn.domain.model.Gender
import com.example.minn.domain.model.User
import com.example.minn.domain.usecase.authUseCase.SignOutUseCase
import com.example.minn.domain.usecase.userUseCase.GetUserUseCase
import com.example.minn.domain.usecase.userUseCase.UpdateUserUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val auth: FirebaseAuth
): ViewModel(){
    private val _state = MutableStateFlow(ProfileUIState())
    val state = _state.asStateFlow()

    init{
        getUser()
    }

    fun onNameChange(value: String) {
        _state.value = _state.value.copy(name = value)
    }

    fun onSurnameChange(value: String) {
        _state.value = _state.value.copy(surname = value)
    }

    fun onBioChange(value: String) {
        _state.value = _state.value.copy(bio = value)
    }

    fun onAvatarChange(value: String) {
        _state.value = _state.value.copy(avatar = value)
    }

    fun onGenderChange(value: String) {
        _state.value = _state.value.copy(surname = value)
    }




    private fun getUser() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            getUserUseCase(uid).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }

                    is Response.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            name = response.data.name,
                            surname = response.data.surname,
                            bio = response.data.bio,
                            avatar = response.data.avatar,
                            gender = response.data.gender
                        )
                    }

                    is Response.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = response.message
                        )
                    }
                }
            }
        }
    }


    fun updateUser(){
        val uid = auth.currentUser?.uid ?: return
        val user = User(
            uid = uid,
            name = _state.value.name,
            surname = _state.value.surname,
            bio = _state.value.bio,
            avatar = _state.value.avatar,
            gender = _state.value.gender
        )
        viewModelScope.launch {
            updateUserUseCase(user = user)
        }
    }

    fun signOut(){
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    private fun fillFields(user: User) {
        _state.value = _state.value.copy(
            name = user.name,
            surname = user.surname,
            bio = user.bio,
            avatar = user.avatar,
            gender = user.gender
        )
    }
}

data class ProfileUIState(
    val name: String = "",
    val surname: String = "",
    val bio: String = "",
    val avatar: String = "",
    val gender: Gender = Gender.NOT_SPECIFIED,
    val isLoading: Boolean = true,
    val error: String? = null
)
