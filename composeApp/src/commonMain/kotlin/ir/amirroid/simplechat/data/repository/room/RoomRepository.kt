package ir.amirroid.simplechat.data.repository.room

import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.room.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun getAllRooms(): Response<DefaultRequiredResponse<List<Room>>, NetworkErrors>
    suspend fun fetchAndSaveAllRooms(): Response<DefaultRequiredResponse<List<Room>>, NetworkErrors>
    suspend fun saveRooms(rooms: List<Room>, withClearData: Boolean = false)
    fun getAllRoomsFromLocal(): Flow<List<Room>>
    suspend fun updateLastMessageRoom(roomId: Long, message: String)
}