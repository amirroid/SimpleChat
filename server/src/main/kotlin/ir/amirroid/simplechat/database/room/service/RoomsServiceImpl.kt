package ir.amirroid.simplechat.database.room.service

import ir.amirroid.simplechat.database.message.MessageTable
import ir.amirroid.simplechat.database.room.RoomTable
import ir.amirroid.simplechat.database.room_member.RoomMemberTable
import ir.amirroid.simplechat.database.room_member.mapper.toRoomMember
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.exceptions.internalServerError
import ir.amirroid.simplechat.exceptions.notFoundError
import ir.amirroid.simplechat.models.room.Room
import ir.amirroid.simplechat.utils.dbQuery
import org.jetbrains.exposed.v1.core.Alias
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.leftJoin
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class RoomsServiceImpl(database: Database) : RoomsService {


    override suspend fun getUserRooms(userId: String) = dbQuery {
        val allMembers = RoomMemberTable.alias("all_members")
        val rows = fetchRoomRows(userId, allMembers)
        val grouped = rows.groupBy { it[RoomTable.id].value }
        grouped.map { (_, roomRows) -> buildRoom(roomRows, allMembers, userId) }
    }

    override suspend fun getUserRoomById(userId: String, roomId: Long) = dbQuery {
        val allMembers = RoomMemberTable.alias("all_members")

        val rows = fetchRoomRows(userId, allMembers)
            .filter { it[RoomTable.id].value == roomId }

        if (rows.isEmpty()) notFoundError("Can't fetch room")

        buildRoom(rows, allMembers, userId)
    }


    private fun fetchRoomRows(userId: String, allMembers: Alias<RoomMemberTable>) =
        RoomMemberTable
            .join(
                RoomTable,
                JoinType.INNER,
                additionalConstraint = { RoomMemberTable.roomId eq RoomTable.id })
            .join(
                allMembers,
                JoinType.INNER,
                additionalConstraint = { allMembers[RoomMemberTable.roomId] eq RoomTable.id })
            .join(
                UserTable,
                JoinType.INNER,
                additionalConstraint = { allMembers[RoomMemberTable.userId] eq UserTable.userId })
            .selectAll()
            .where { RoomMemberTable.userId eq userId }
            .toList()

    private fun buildRoom(
        rows: List<ResultRow>,
        allMembers: Alias<RoomMemberTable>,
        myUserId: String
    ): Room {
        val roomRow = rows.first()
        val isPrivate = roomRow[RoomTable.isPrivate]
        val members = rows.map { it.toRoomMember(allMembers, myUserId) }
        val name =
            if (isPrivate) members.firstOrNull { !it.isMe }?.user?.username else roomRow[RoomTable.name]
        val lastMessage = MessageTable.select(MessageTable.content)
            .orderBy(MessageTable.createdAt, SortOrder.DESC)
            .firstOrNull()?.getOrNull(MessageTable.content)

        return Room(
            name = name,
            isPrivate = isPrivate,
            createdAt = roomRow[RoomTable.createdAt],
            id = roomRow[RoomTable.id].value,
            members = if (isPrivate) members else members.filter { it.isMe },
            lastMessage = lastMessage
        )
    }


    override suspend fun createRoom(createdByUserId: String): Long = dbQuery {
        RoomTable.insert {
            it[RoomTable.createdBy] = createdByUserId
            it[RoomTable.isPrivate] = true
        }.resultedValues?.firstOrNull()?.get(RoomTable.id)?.value
            ?: internalServerError("Failed to save room")
    }

    init {
        transaction(database) {
            SchemaUtils.create(RoomTable, RoomMemberTable)
        }
    }
}