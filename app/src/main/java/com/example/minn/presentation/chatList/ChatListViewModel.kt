package com.example.minn.presentation.chatList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.Util.Response
import com.example.minn.domain.model.User
import com.example.minn.domain.usecase.authUseCase.SignOutUseCase
import com.example.minn.domain.usecase.chatUseCase.GetOrCreateChatUseCase
import com.example.minn.domain.usecase.userUseCase.GetAllUsersUseCase
import com.example.minn.domain.usecase.userUseCase.SearchUserByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val searchUserByNameUseCase: SearchUserByNameUseCase,
    private val getOrCreateChatUseCase: GetOrCreateChatUseCase
): ViewModel() {

    private val _state = MutableStateFlow(UsersUiState())
    val state = _state.asStateFlow()

    private val _openChat = MutableSharedFlow<String>()
    val openChat = _openChat.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun onSearchChange(text: String){
        Log.d("Search", "onSearchChange")
        _searchQuery.value = text
    }
    fun onSearchClear(){
        Log.d("Search", "onSearchClear")
        _searchQuery.value = ""
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val users = searchQuery
        .debounce(300)
        .flatMapLatest {query->
            searchUserByNameUseCase(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        getAllUsers()
    }

    fun getAllUsers(){
        viewModelScope.launch {
            getAllUsersUseCase().collect {response ->
                when(response){
                    is Response.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }
                    is Response.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = response.message
                        )
                    }
                    is Response.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            users = response.data
                        )
                    }
                }
            }
        }
    }
    fun signOut(){
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    fun openChat(opponentUid: String){
        viewModelScope.launch {
            val chatId = getOrCreateChatUseCase(opponentUid)
            _openChat.emit(chatId)
        }
    }

}

data class UsersUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)