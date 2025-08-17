package ir.amirroid.simplechat.database.room_member.service

import ir.amirroid.simplechat.data.models.room.RoomMember

interface RoomMemberService {
    suspend fun getRoomMember(roomId: Long, userId: String): RoomMember
}