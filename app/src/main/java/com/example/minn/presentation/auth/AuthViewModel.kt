package com.example.minn.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.Util.Response
import com.example.minn.domain.usecase.authUseCase.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
): ViewModel() {

    private val _state = MutableStateFlow((AuthUIState()))
    val state = _state.asStateFlow()

    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun onNameChange(newName: String){
        name = newName
    }
    fun onEmailChange(newEmail: String){
        email = newEmail
    }

    fun onPasswordChange(newPassword: String){
        password = newPassword
    }

    init{
        getAuthState()
    }

    private fun getAuthState(){
        viewModelScope.launch {
            authUseCases.getAuthState().collect {isAuth->
                _state.value = _state.value.copy(
                    isLoading = false,
                    isAuthorized = isAuth)

            }
        }
    }

    fun signUp(email: String, password: String, name: String){
        viewModelScope.launch {
            authUseCases.signUp(email, password, name).collect { response ->
                handleResponse(response)
            }
        }
    }
    fun signIn(email: String, password: String){
        viewModelScope.launch {
            authUseCases.signIn(email, password).collect { response ->
                handleResponse(response)
            }
        }
    }


    private fun handleResponse(response: Response<Boolean>){
        when (response){
            is Response.Loading->{
                _state.value = AuthUIState(isLoading = true)
            }
            is Response.Success->{
                _state.value = AuthUIState(isAuthorized = response.data)
            }
            is Response.Error->{
                _state.value = AuthUIState(error = response.message)
            }
        }
    }

}


data class AuthUIState(
    val isLoading: Boolean = true,
    val isAuthorized: Boolean = false,
    val error: String? = null
)