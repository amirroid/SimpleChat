package ir.amirroid.simplechat.data.mappers

import ir.amirroid.simplechat.data.database.relations.StatusWithUser
import ir.amirroid.simplechat.models.message.MessageStatus

fun StatusWithUser.toMessageStatus() = MessageStatus(
    messageId = status.messageId,
    user = user.toUser(),
    status = status.status,
    updatedAt = status.updatedAt
)