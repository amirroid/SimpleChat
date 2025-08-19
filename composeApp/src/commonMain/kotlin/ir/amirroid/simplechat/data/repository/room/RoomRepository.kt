package ir.amirroid.simplechat.data.repository.room

import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.room.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun getAllRooms(): Response<DefaultRequiredResponse<List<Room>>, NetworkErrors>
    suspend fun fetchAndSaveAllRooms(): Response<DefaultRequiredResponse<List<Room>>, NetworkErrors>
    fun getAllRoomsFromLocal(): Flow<List<Room>>
}