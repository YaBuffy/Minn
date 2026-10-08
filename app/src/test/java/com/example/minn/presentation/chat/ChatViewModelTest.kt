package com.example.minn.presentation.chat

import com.example.minn.domain.model.Message
import com.example.minn.testing.FakeChatRepository
import com.example.minn.testing.FakeUserRepository
import com.example.minn.testing.MainDispatcherRule
import com.example.minn.domain.usecase.chatUseCase.LoadLastMessagesUseCase
import com.example.minn.domain.usecase.chatUseCase.LoadOlderMessagesUseCase
import com.example.minn.domain.usecase.chatUseCase.ObserveNewMessagesUseCase
import com.example.minn.domain.usecase.chatUseCase.SendMessageUseCase
import com.example.minn.domain.usecase.userUseCase.GetUserUseCase
import com.google.firebase.Timestamp
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val chatRepository = FakeChatRepository()
    private val userRepository = FakeUserRepository()

    private fun message(id: String, text: String = "text $id") = Message(
        id = id,
        senderId = "me",
        text = text,
        timestamp = Timestamp(Date())
    )

    private fun createViewModel(): ChatViewModel = ChatViewModel(
        observeNewMessagesUseCase = ObserveNewMessagesUseCase(chatRepository),
        loadOlderMessagesUseCase = LoadOlderMessagesUseCase(chatRepository),
        sendMessageUseCase = SendMessageUseCase(chatRepository),
        getUserUseCase = GetUserUseCase(userRepository),
        loadLastMessagesUseCase = LoadLastMessagesUseCase(chatRepository),
        resources = mockk(relaxed = true),
        auth = mockk(relaxed = true) {
            every { currentUser?.uid } returns "me"
        }
    )

    @Before
    fun setUp() {
        chatRepository.initialMessages = listOf(message("m3"), message("m2"), message("m1"))
    }

    @Test
    fun `loads the initial page newest first`() = runTest {
        val vm = createViewModel()
        vm.startChat("chat1")
        advanceUntilIdle()

        assertEquals(listOf("m3", "m2", "m1"), vm.messages.value.map { it.id })
        assertEquals(1, chatRepository.loadLastMessagesCalls)
    }

    /**
     * `observeNewMessages` is limited to a single document by the repository, so
     * a snapshot can redeliver a message already in the list. The ViewModel
     * deduplicates by id before prepending.
     */
    @Test
    fun `new messages are prepended without duplicating existing ones`() = runTest {
        val vm = createViewModel()
        vm.startChat("chat1")
        advanceUntilIdle()

        chatRepository.newMessagesResponse.value = listOf(message("m3"))
        advanceUntilIdle()
        chatRepository.newMessagesResponse.value = listOf(message("m4"))
        advanceUntilIdle()

        assertEquals(listOf("m4", "m3", "m2", "m1"), vm.messages.value.map { it.id })
    }

    @Test
    fun `switching chat reloads the initial page`() = runTest {
        val vm = createViewModel()
        vm.startChat("chat1")
        advanceUntilIdle()
        vm.startChat("chat2")
        advanceUntilIdle()

        assertEquals(2, chatRepository.loadLastMessagesCalls)
    }

    @Test
    fun `starting the same chat twice does not reload`() = runTest {
        val vm = createViewModel()
        vm.startChat("chat1")
        advanceUntilIdle()
        vm.startChat("chat1")
        advanceUntilIdle()

        assertEquals(1, chatRepository.loadLastMessagesCalls)
    }

    @Test
    fun `loadOlder appends and stops when the page is empty`() = runTest {
        chatRepository.initialCursor = mockk(relaxed = true)
        chatRepository.olderMessagesPages += listOf(message("m0")) to mockk(relaxed = true)

        val vm = createViewModel()
        vm.startChat("chat1")
        advanceUntilIdle()

        vm.loadOlder()
        advanceUntilIdle()
        assertEquals(listOf("m3", "m2", "m1", "m0"), vm.messages.value.map { it.id })

        // Repository returns an empty page: history is exhausted.
        vm.loadOlder()
        advanceUntilIdle()

        // Cursor is nulled, so a third call must not hit the repository again.
        val pagesAfterExhaustion = chatRepository.olderMessagesPages.size
        vm.loadOlder()
        advanceUntilIdle()

        assertEquals(pagesAfterExhaustion, chatRepository.olderMessagesPages.size)
    }

    @Test
    fun `loadOlder is a no-op before the first page is loaded`() = runTest {
        val vm = createViewModel()

        vm.loadOlder()
        advanceUntilIdle()

        assertTrue(vm.messages.value.isEmpty())
    }

    @Test
    fun `sending a message forwards it and clears the draft`() = runTest {
        val vm = createViewModel()
        vm.startChat("chat1")
        advanceUntilIdle()

        vm.onChatChange("hello")
        vm.sendMessage("hello", "chat1")
        advanceUntilIdle()

        assertEquals(listOf("chat1" to "hello"), chatRepository.sentMessages)
        assertEquals("", vm.chatText.value)
    }
}