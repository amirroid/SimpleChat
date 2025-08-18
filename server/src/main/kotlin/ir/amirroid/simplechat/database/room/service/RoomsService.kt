package ir.amirroid.simplechat.database.room.service

import ir.amirroid.simplechat.models.room.Room

interface RoomsService {
    suspend fun getUserRooms(userId: String): List<Room>
}