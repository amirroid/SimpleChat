package ir.amirroid.simplechat.data.repository.room

import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.room.Room

interface RoomRepository {
    suspend fun getAllRooms(): Response<DefaultRequiredResponse<List<Room>>, NetworkErrors>
}