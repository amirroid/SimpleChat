package ir.amirroid.simplechat.database.room.service

import ir.amirroid.simplechat.models.room.Room

interface RoomsService {
    suspend fun getUserRooms(userId: String): List<Room>
    suspend fun createRoom(createdByUserId: String): Long
    suspend fun getUserRoomById(userId: String, roomId: Long): Room
}