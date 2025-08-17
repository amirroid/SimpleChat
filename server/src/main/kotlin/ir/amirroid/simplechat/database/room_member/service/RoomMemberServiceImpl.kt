package ir.amirroid.simplechat.database.room_member.service

import ir.amirroid.simplechat.data.models.room.RoomMember
import ir.amirroid.simplechat.database.room_member.RoomMemberTable
import ir.amirroid.simplechat.database.room_member.mapper.toRoomMember
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.exceptions.notFoundError
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.selectAll

class RoomMemberServiceImpl : RoomMemberService {
    override suspend fun getRoomMember(roomId: Long, userId: String): RoomMember {
        return RoomMemberTable
            .innerJoin(UserTable)
            .selectAll()
            .where {
                (RoomMemberTable.userId eq userId) and (RoomMemberTable.roomId eq roomId)
            }
            .map { it.toRoomMember(userId) }
            .firstOrNull() ?: notFoundError("Member does not exist")
    }
}