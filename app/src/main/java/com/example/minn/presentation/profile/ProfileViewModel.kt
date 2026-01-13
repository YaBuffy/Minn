package com.example.minn.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.Util.Response
import com.example.minn.domain.model.Gender
import com.example.minn.domain.model.User
import com.example.minn.domain.usecase.authUseCase.DeleteAccountUseCase
import com.example.minn.domain.usecase.authUseCase.ReauthenticateUseCase
import com.example.minn.domain.usecase.authUseCase.SignOutUseCase
import com.example.minn.domain.usecase.userUseCase.GetUserUseCase
import com.example.minn.domain.usecase.userUseCase.UpdateUserUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import com.google.firebase.Timestamp
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val reauthenticateUseCase: ReauthenticateUseCase,
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

    fun onGenderChange(value: Gender) {
        _state.value = _state.value.copy(gender = value)
    }

    fun onBirthDateChange(value: LocalDate){
        _state.value = _state.value.copy(birthDate = value)
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
                        Log.d("Time", "response.data.birthday - ${response.data.birthdate}")
                        val birthDateLocal: LocalDate? = response.data.birthdate?.toDate()
                            ?.toInstant()
                            ?.atZone(ZoneId.systemDefault())
                            ?.toLocalDate()
                        Log.d("Time", "birthDateLocal - ${birthDateLocal.toString()}")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            name = response.data.name,
                            surname = response.data.surname,
                            email = response.data.email,
                            bio = response.data.bio,
                            avatar = response.data.avatar,
                            gender = response.data.gender,
                            birthDate = birthDateLocal
                        )
                        Log.d("Time", "_state.value.birthDate - ${_state.value.birthDate}")
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
        val birthDateTimestamp: Timestamp? =
            _state.value.birthDate?.let { localDate ->
                val instant = localDate
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()

                Timestamp(Date.from(instant))
            }
        Log.d("Time", "birthDateTimestamp - ${birthDateTimestamp}")
        val user = User(
            uid = uid,
            name = _state.value.name,
            nameLower = _state.value.name.lowercase(),
            surname = _state.value.surname,
            email = _state.value.email,
            bio = _state.value.bio,
            avatar = _state.value.avatar,
            gender = _state.value.gender,
            birthdate = birthDateTimestamp
        )
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, success = false, error = null)
            try {
                updateUserUseCase(user = user)
                _state.value = _state.value.copy(isLoading = false, success = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun signOut(){
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    fun hideReAuthDialog() {
        _state.value = _state.value.copy(showReAuthDialog = false)
    }

    fun reauthenticateAndDelete(password: String) {
        val email = auth.currentUser?.email ?: return

        viewModelScope.launch {
            when (reauthenticateUseCase(email, password)) {
                is Response.Success -> {
                    _state.value = _state.value.copy(isLoading = false, error = null)
                    deleteAccount()
                }
                is Response.Error -> {
                    _state.value = _state.value.copy(isLoading = false, error = "Wrong password")
                }
                else -> {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                }
            }
        }
    }

    fun deleteAccount(){
        viewModelScope.launch {
            when (val result = deleteAccountUseCase()) {
                is Response.Success -> {
                    _state.value = _state.value.copy(isLoading = false, isDeleted = true)
                }

                is Response.Error -> {
                    if (result.message == "RECENT_LOGIN_REQUIRED") {
                        _state.value = _state.value.copy(isLoading = false,showReAuthDialog = true)
                    } else {
                        _state.value = _state.value.copy(isLoading = false,error = result.message)
                    }
                }
                else -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

//    private fun fillFields(user: User) {
//        val birthDateLocal: LocalDate? = user.birthdate?.toDate()
//            ?.toInstant()
//            ?.atZone(ZoneId.systemDefault())
//            ?.toLocalDate()
//        _state.value = _state.value.copy(
//            name = user.name,
//            surname = user.surname,
//            bio = user.bio,
//            avatar = user.avatar,
//            gender = user.gender,
//            birthDate = birthDateLocal
//        )
//    }
}

data class ProfileUIState(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val bio: String = "",
    val avatar: String = "",
    val gender: Gender = Gender.NOT_SPECIFIED,
    val birthDate: LocalDate? = null,
    val isLoading: Boolean = true,
    val success: Boolean = false,
    val error: String? = null,
    val isDeleted: Boolean = false,
    val showReAuthDialog: Boolean = false
)
