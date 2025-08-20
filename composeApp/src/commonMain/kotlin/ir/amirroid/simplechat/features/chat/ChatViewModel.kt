package ir.amirroid.simplechat.features.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.amirroid.simplechat.data.repository.chat.ChatRepository
import ir.amirroid.simplechat.data.repository.user.UserRepository
import ir.amirroid.simplechat.data.service.GlobalStreamSocketService
import ir.amirroid.simplechat.models.message.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val streamSocket: GlobalStreamSocketService,
    userRepository: UserRepository,
) : ViewModel() {
    private val roomId = savedStateHandle.get<Long>("roomId")!!
    val name = savedStateHandle.get<String>("name")!!

    val sentSeenRequests = mutableListOf<Long>()

    val myUser = userRepository.user


    var newMessage by mutableStateOf("")

    val messages = chatRepository.getAllChatsFromLocal(roomId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isUpdatingMessages = MutableStateFlow(false)
    val isUpdatingMessages = _isUpdatingMessages.asStateFlow()

    init {
        fetchNewMessages()
    }

    fun fetchNewMessages() = viewModelScope.launch(Dispatchers.IO) {
        _isUpdatingMessages.update { true }
        chatRepository.fetchAllChatsAndSave(roomId)
        _isUpdatingMessages.update { false }

    }

    fun sendMessage() {
        streamSocket.sendMessage(newMessage, roomId)
        newMessage = ""
    }


    fun seenMessage(message: Message) {
        if (sentSeenRequests.contains(message.id)) return
        sentSeenRequests.add(message.id)
        streamSocket.seenMessage(message.id)
    }
}