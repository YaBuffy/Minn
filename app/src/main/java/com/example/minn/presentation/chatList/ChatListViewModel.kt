package com.example.minn.presentation.chatList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minn.Util.Response
import com.example.minn.domain.model.Chat
import com.example.minn.domain.model.User
import com.example.minn.domain.usecase.chatUseCase.GetOrCreateChatUseCase
import com.example.minn.domain.usecase.chatUseCase.LoadLastChatsUseCase
import com.example.minn.domain.usecase.userUseCase.GetAllUsersUseCase
import com.example.minn.domain.usecase.userUseCase.GetUserUseCase
import com.example.minn.domain.usecase.userUseCase.SearchUserByNameUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val searchUserByNameUseCase: SearchUserByNameUseCase,
    private val getOrCreateChatUseCase: GetOrCreateChatUseCase,
    private val loadLastChatsUseCase: LoadLastChatsUseCase,
    private val getUserUseCase: GetUserUseCase,
    val auth: FirebaseAuth
): ViewModel() {

    private val _state = MutableStateFlow(UsersUiState())
    val state = _state.asStateFlow()

    private val _openChat = MutableSharedFlow<OpenChatEvent>()
    val openChat = _openChat.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats = _chats.asStateFlow()
    val currentUser = auth.currentUser!!.uid

    private val _opponents = MutableStateFlow<Map<String, User>>(emptyMap())
    val opponents = _opponents.asStateFlow()
    private var chatsJob: Job? = null


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

    fun openChat(opponentUid: String){
        viewModelScope.launch {
            val chatId = getOrCreateChatUseCase(opponentUid).id
            Log.d("ChatRepo", "chatId in vm openChat is ${chatId}")
            _openChat.emit(
                OpenChatEvent(
                chatId = chatId,
                opponentUid = opponentUid
                )
            )
        }
    }
    fun loadLastChats(){
        chatsJob?.cancel()
        viewModelScope.launch {
            loadLastChatsUseCase().collect { chat ->
                _chats.value = chat
            }
        }
    }

    fun loadOpponent(chat: Chat) {
        val opponentId = chat.members.first { it != currentUser }

        // Если уже загружен — не грузим
        if (_opponents.value.containsKey(opponentId)) return

        viewModelScope.launch {
            getUserUseCase(opponentId).collect { response ->
                if (response is Response.Success) {
                    _opponents.update {map ->
                        map + (opponentId to response.data)
                    }
                }
            }
        }
    }

}

data class UsersUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)

data class OpenChatEvent(
    val chatId: String,
    val opponentUid: String
)