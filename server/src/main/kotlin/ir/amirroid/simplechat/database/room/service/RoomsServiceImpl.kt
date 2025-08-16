package ir.amirroid.simplechat.database.room.service

import ir.amirroid.simplechat.data.models.room.Room
import ir.amirroid.simplechat.database.room.RoomMemberTable
import ir.amirroid.simplechat.database.room.RoomTable
import ir.amirroid.simplechat.database.room.mapper.toRoomMember
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.utils.dbQuery
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class RoomsServiceImpl(database: Database) : RoomsService {
    override suspend fun getUserRooms(userId: String) = dbQuery {
        val allMembers = RoomMemberTable.alias("all_members")

        val query = RoomMemberTable
            .join(RoomTable, JoinType.INNER, additionalConstraint = {
                RoomMemberTable.roomId eq RoomTable.id
            })
            .join(allMembers, JoinType.INNER, additionalConstraint = {
                allMembers[RoomMemberTable.roomId] eq RoomTable.id
            })
            .join(UserTable, JoinType.INNER, additionalConstraint = {
                allMembers[RoomMemberTable.userId] eq UserTable.userId
            })
            .selectAll()
            .where { RoomMemberTable.userId eq userId }

        val grouped = query.groupBy { it[RoomTable.id].value }

        grouped.map { (_, rows) ->
            val roomRow = rows.first()
            val isPrivate = roomRow[RoomTable.isPrivate]

            val members = rows.map { it.toRoomMember(allMembers, userId) }

            val name = if (isPrivate) {
                members.firstOrNull { !it.isMe }?.user?.username
            } else roomRow[RoomTable.name]

            Room(
                name = name,
                isPrivate = isPrivate,
                createdAt = roomRow[RoomTable.createdAt],
                members = if (isPrivate) {
                    members
                } else {
                    members.filter { it.isMe }
                }
            )
        }
    }

    init {
        transaction(database) {
            SchemaUtils.create(RoomTable, RoomMemberTable)
        }
    }
}