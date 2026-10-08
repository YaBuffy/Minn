package com.example.minn.testing

import com.example.minn.Util.Response
import com.example.minn.domain.model.Chat
import com.example.minn.domain.model.Message
import com.example.minn.domain.model.User
import com.example.minn.domain.repository.AuthRepository
import com.example.minn.domain.repository.ChatRepository
import com.example.minn.domain.repository.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

/**
 * Hand-written fakes for the repository interfaces. Real Firebase clients are
 * unusable in JVM unit tests (they need a native runtime), and the repository
 * contracts are plain interfaces, so fakes cover the same ground as mocks here.
 *
 * Each fake records its calls so tests can assert on orchestration, e.g. that
 * sign-in flips the online flag, which lives in the use case rather than the repo.
 */
class FakeAuthRepository : AuthRepository {

    var currentUserSignedIn = true
    var authState: MutableStateFlow<Boolean> = MutableStateFlow(currentUserSignedIn)

    var signInResponse: Flow<Response<Boolean>> = flowOfSuccess()
    var signUpResponse: Flow<Response<Boolean>> = flowOfSuccess()
    var deleteAccountResponse: Response<Boolean> = Response.Success(true)
    var reauthenticateResponse: Response<Boolean> = Response.Success(true)

    var signInCalls = 0
    var signUpCalls = 0
    var signOutCalls = 0
    var deleteAccountCalls = 0

    private fun flowOfSuccess(): Flow<Response<Boolean>> =
        kotlinx.coroutines.flow.flowOf(Response.Loading, Response.Success(true))

    override fun isUserAuth(): Boolean = currentUserSignedIn

    override fun getAuthState(): Flow<Boolean> = authState

    override fun signUp(email: String, password: String, name: String): Flow<Response<Boolean>> {
        signUpCalls++
        lastSignUpName = name
        return signUpResponse
    }

    override fun signIn(email: String, password: String): Flow<Response<Boolean>> {
        signInCalls++
        return signInResponse
    }

    override suspend fun signOut() {
        signOutCalls++
    }

    override suspend fun deleteAccount(): Response<Boolean> {
        deleteAccountCalls++
        return deleteAccountResponse
    }

    override suspend fun reauthenticate(email: String, password: String): Response<Boolean> =
        reauthenticateResponse

    var lastSignUpName: String? = null
}

class FakeUserRepository : UserRepository {

    var onlineUpdates = mutableListOf<Boolean>()
    var createdUsers = mutableListOf<User>()

    var userResponse: MutableStateFlow<Response<User>> = MutableStateFlow(Response.Loading)
    var allUsersResponse: MutableStateFlow<Response<List<User>>> =
        MutableStateFlow(Response.Loading)
    var searchResults: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())

    override suspend fun createUser(user: User) {
        createdUsers += user
    }

    override suspend fun updateUser(user: User) = Unit

    override fun getUser(uid: String): Flow<Response<User>> = userResponse

    override fun getAllUsers(): Flow<Response<List<User>>> = allUsersResponse

    override fun searchUserByName(query: String): Flow<List<User>> = searchResults

    override suspend fun setOnline(online: Boolean) {
        onlineUpdates += online
    }
}

class FakeChatRepository : ChatRepository {

    var lastChatsResponse: MutableStateFlow<List<Chat>> = MutableStateFlow(emptyList())
    var newMessagesResponse: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())

    var initialMessages = emptyList<Message>()
    var initialCursor: DocumentSnapshot? = null
    var olderMessagesPages = mutableListOf<Pair<List<Message>, DocumentSnapshot?>>()
    var sentMessages = mutableListOf<Pair<String, String>>()
    var chatToCreate = Chat(id = "chat1", members = listOf("me", "other"))

    var loadLastMessagesCalls = 0

    override fun observeNewMessages(chatId: String): Flow<List<Message>> = newMessagesResponse

    override suspend fun sendMessage(chatId: String, text: String) {
        sentMessages += chatId to text
    }

    override suspend fun getOrCreateChat(otherUserId: String): Chat = chatToCreate

    override fun loadLastChats(): Flow<List<Chat>> = lastChatsResponse

    override suspend fun loadLastMessages(
        chatId: String
    ): Pair<List<Message>, DocumentSnapshot?> {
        loadLastMessagesCalls++
        return initialMessages to initialCursor
    }

    override suspend fun loadOlderMessages(
        chatId: String,
        lastSnapshot: DocumentSnapshot?
    ): Pair<List<Message>, DocumentSnapshot?> {
        if (olderMessagesPages.isEmpty()) return emptyList<Message>() to null
        return olderMessagesPages.removeAt(0)
    }
}