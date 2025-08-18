package ir.amirroid.simplechat.database.room_member.service

import ir.amirroid.simplechat.database.message.MessageTable
import ir.amirroid.simplechat.database.room_member.RoomMemberTable
import ir.amirroid.simplechat.database.room_member.mapper.toRoomMember
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.exceptions.notFoundError
import ir.amirroid.simplechat.utils.dbQuery
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

class RoomMemberServiceImpl : RoomMemberService {
    override suspend fun getRoomMember(roomId: Long, userId: String) = dbQuery {
        RoomMemberTable
            .innerJoin(UserTable)
            .selectAll()
            .where {
                (RoomMemberTable.userId eq userId) and (RoomMemberTable.roomId eq roomId)
            }
            .map { it.toRoomMember(userId) }
            .firstOrNull() ?: notFoundError("Member does not exist")
    }

    override suspend fun getAllRoomMemberIds(roomId: Long) = dbQuery {
        RoomMemberTable
            .select(RoomMemberTable.userId)
            .where { RoomMemberTable.roomId eq roomId }
            .map { it[RoomMemberTable.userId] }
    }

    override suspend fun getAllRoomMemberIdsFromMessage(messageId: Long): List<String> = dbQuery {
        val roomId = MessageTable
            .select(MessageTable.roomId)
            .where { MessageTable.id eq messageId }
            .map { it[MessageTable.roomId] }
            .firstOrNull()?.value ?: return@dbQuery emptyList()

        getAllRoomMemberIds(roomId)
    }
}