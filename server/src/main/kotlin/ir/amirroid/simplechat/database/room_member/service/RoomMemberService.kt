package ir.amirroid.simplechat.database.room_member.service

import ir.amirroid.simplechat.models.room.MemberRole
import ir.amirroid.simplechat.models.room.RoomMember


data class UserIdWithRole(
    val userId: String,
    val role: MemberRole
)

interface RoomMemberService {
    suspend fun getRoomMember(roomId: Long, userId: String): RoomMember
    suspend fun getAllRoomMemberIds(roomId: Long): List<String>
    suspend fun getAllRoomMemberIdsFromMessage(messageId: Long): List<String>
    suspend fun addMembers(roomId: Long, members: List<UserIdWithRole>)
}