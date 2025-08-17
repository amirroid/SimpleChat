package ir.amirroid.simplechat.database.message.services

import ir.amirroid.simplechat.database.message.MessageTable
import ir.amirroid.simplechat.database.message.mapper.toMessage
import ir.amirroid.simplechat.database.room.RoomMemberTable
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.utils.dbQuery
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class MessagesServiceImpl(database: Database) : MessagesService {
    override suspend fun getAllMessages(
        roomId: Long,
        myUserId: String
    ) = dbQuery {
        val roomMemberAlias = RoomMemberTable.alias("sender_member")

        MessageTable
            .join(
                roomMemberAlias,
                JoinType.INNER,
                additionalConstraint = {
                    (MessageTable.roomId eq roomMemberAlias[RoomMemberTable.roomId]) and
                            (MessageTable.senderUserId eq roomMemberAlias[RoomMemberTable.userId])
                }
            )
            .join(
                UserTable,
                JoinType.INNER,
                additionalConstraint = {
                    roomMemberAlias[RoomMemberTable.userId] eq UserTable.userId
                }
            )
            .selectAll()
            .where { MessageTable.roomId eq roomId }
            .map { it.toMessage(roomMemberAlias, myUserId) }
    }

    init {
        transaction(database) {
            SchemaUtils.create(MessageTable)
        }
    }
}