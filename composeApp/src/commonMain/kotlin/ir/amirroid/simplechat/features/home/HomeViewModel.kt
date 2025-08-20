package ir.amirroid.simplechat.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.amirroid.simplechat.data.repository.room.RoomRepository
import ir.amirroid.simplechat.data.service.GlobalStreamSocketService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val roomRepository: RoomRepository,
    private val globalStreamSocketService: GlobalStreamSocketService
) : ViewModel() {
    val rooms = roomRepository.getAllRoomsFromLocal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var isCreteRoomDialogVisible by mutableStateOf(false)

    init {
        loadNewRooms()
    }

    fun loadNewRooms() = viewModelScope.launch(Dispatchers.IO) {
        roomRepository.fetchAndSaveAllRooms()
    }

    fun createRoom(userId: String) = globalStreamSocketService.createRoom(userId)
}