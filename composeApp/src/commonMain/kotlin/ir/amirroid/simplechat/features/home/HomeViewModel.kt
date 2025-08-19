package ir.amirroid.simplechat.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.amirroid.simplechat.data.repository.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val roomRepository: RoomRepository
) : ViewModel() {
    val rooms = roomRepository.getAllRoomsFromLocal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadNewRooms()
    }

    fun loadNewRooms() = viewModelScope.launch(Dispatchers.IO) {
        roomRepository.fetchAndSaveAllRooms()
    }
}