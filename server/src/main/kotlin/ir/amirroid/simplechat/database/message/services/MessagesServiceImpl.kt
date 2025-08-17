package ir.amirroid.simplechat.database.message.services

import ir.amirroid.simplechat.database.message.MessageStatusTable
import ir.amirroid.simplechat.database.message.MessageTable
import ir.amirroid.simplechat.database.message.mapper.toMessage
import ir.amirroid.simplechat.database.message.mapper.toMessageStatus
import ir.amirroid.simplechat.database.room.RoomMemberTable
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.utils.dbQuery
import org.jetbrains.exposed.v1.core.Alias
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
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
        val senderAlias = RoomMemberTable.alias("sender_member")
        val senderUserAlias = UserTable.alias("sender_user")
        val statusAlias = MessageStatusTable.alias("status_alias")
        val statusUserAlias = UserTable.alias("status_user")

        val rows = fetchMessagesWithStatuses(
            roomId,
            senderAlias,
            senderUserAlias,
            statusAlias,
            statusUserAlias
        )

        rows.groupBy { it[MessageTable.id].value }
            .map { (_, groupedRows) ->
                val firstRow = groupedRows.first()
                val statuses =
                    groupedRows.mapNotNull { it.toMessageStatus(statusAlias, statusUserAlias) }
                firstRow.toMessage(senderAlias, senderUserAlias, myUserId, statuses)
            }
    }


    private fun fetchMessagesWithStatuses(
        roomId: Long,
        senderAlias: Alias<RoomMemberTable>,
        senderUserAlias: Alias<UserTable>,
        statusAlias: Alias<MessageStatusTable>,
        statusUserAlias: Alias<UserTable>
    ): List<ResultRow> {
        return MessageTable
            .join(
                senderAlias, JoinType.INNER,
                additionalConstraint = {
                    (MessageTable.roomId eq senderAlias[RoomMemberTable.roomId]) and
                            (MessageTable.senderUserId eq senderAlias[RoomMemberTable.userId])
                }
            )
            .join(
                senderUserAlias, JoinType.INNER,
                additionalConstraint = { senderAlias[RoomMemberTable.userId] eq senderUserAlias[UserTable.userId] }
            )
            .join(
                statusAlias, JoinType.LEFT,
                additionalConstraint = { MessageTable.id eq statusAlias[MessageStatusTable.messageId] }
            )
            .join(
                statusUserAlias, JoinType.LEFT,
                additionalConstraint = { statusAlias[MessageStatusTable.userId] eq statusUserAlias[UserTable.userId] }
            )
            .selectAll()
            .where { MessageTable.roomId eq roomId }
            .orderBy(MessageTable.createdAt, SortOrder.ASC)
            .toList()
    }


    init {
        transaction(database) {
            SchemaUtils.create(MessageTable, MessageStatusTable)
        }
    }
}