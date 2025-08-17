package ir.amirroid.simplechat.database.message_status.service

import ir.amirroid.simplechat.data.models.message.MessageStatus
import ir.amirroid.simplechat.database.message_status.MessageStatusTable
import ir.amirroid.simplechat.database.message_status.MessageStatuses
import ir.amirroid.simplechat.database.message_status.mapper.toMessageStatus
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.utils.dbQuery
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.v1.core.innerJoin
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.upsert
import java.time.LocalDateTime

class MessageStatusServiceImpl : MessageStatusService {
    override suspend fun upsertStatus(
        messageId: Long,
        userId: String,
        status: MessageStatuses
    ) {
        dbQuery {
            MessageStatusTable.upsert {
                it[MessageStatusTable.status] = status
                it[MessageStatusTable.messageId] = messageId
                it[MessageStatusTable.userId] = userId
                it[MessageStatusTable.updatedAt] = LocalDateTime.now().toKotlinLocalDateTime()
            }
        }
    }

    override suspend fun upsertStatuses(
        messageId: Long,
        userWithStatuses: List<UserWithStatus>
    ) {
        val now = LocalDateTime.now().toKotlinLocalDateTime()
        dbQuery {
            MessageStatusTable.batchUpsert(
                data = userWithStatuses,
                onUpdate = { builder ->
                    builder[MessageStatusTable.status] = insertValue(MessageStatusTable.status)
                    builder[MessageStatusTable.updatedAt] =
                        insertValue(MessageStatusTable.updatedAt)
                }
            ) { (userId, status) ->
                this[MessageStatusTable.messageId] = messageId
                this[MessageStatusTable.userId] = userId
                this[MessageStatusTable.status] = status
                this[MessageStatusTable.updatedAt] = now
            }
        }
    }


    override suspend fun getMessageStatuses(messageId: Long) = dbQuery {
        MessageStatusTable
            .innerJoin(UserTable, { MessageStatusTable.userId }, { UserTable.userId })
            .selectAll()
            .where { MessageStatusTable.messageId eq messageId }
            .map {
                it.toMessageStatus()
            }
    }
}