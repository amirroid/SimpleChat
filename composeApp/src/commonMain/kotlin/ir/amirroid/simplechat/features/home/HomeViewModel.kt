package ir.amirroid.simplechat.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.amirroid.simplechat.data.repository.room.RoomRepository
import ir.amirroid.simplechat.data.response.onSuccess
import ir.amirroid.simplechat.models.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms = _rooms.asStateFlow()

    init {
        getAllRooms()
    }

    fun getAllRooms() = viewModelScope.launch(Dispatchers.IO) {
        roomRepository.getAllRooms().onSuccess {
            _rooms.value = it.data
        }
    }
}