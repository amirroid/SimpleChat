package ir.amirroid.simplechat.database.message_status.mapper

import ir.amirroid.simplechat.models.message.MessageStatus
import ir.amirroid.simplechat.database.message_status.MessageStatusTable
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.database.user.mapper.toUser
import org.jetbrains.exposed.v1.core.Alias
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toMessageStatus(
    statusAlias: Alias<MessageStatusTable>,
    statusUserAlias: Alias<UserTable>
): MessageStatus? {
    val messageId = getOrNull(statusAlias[MessageStatusTable.messageId])?.value ?: return null
    return MessageStatus(
        messageId = messageId,
        user = toUser(statusUserAlias),
        status = this[statusAlias[MessageStatusTable.status]],
        updatedAt = this[statusAlias[MessageStatusTable.updatedAt]]
    )
}

fun ResultRow.toMessageStatus(): MessageStatus {
    return MessageStatus(
        messageId = this[MessageStatusTable.messageId].value,
        user = toUser(),
        status = this[MessageStatusTable.status],
        updatedAt = this[MessageStatusTable.updatedAt]
    )
}