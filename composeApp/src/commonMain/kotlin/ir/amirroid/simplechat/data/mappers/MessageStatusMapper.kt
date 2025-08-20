package ir.amirroid.simplechat.data.mappers

import ir.amirroid.simplechat.data.database.entities.MessageStatusEntity
import ir.amirroid.simplechat.models.message.MessageStatus

fun MessageStatus.toEntity() = MessageStatusEntity(
    messageId = messageId,
    userId = user.userId,
    updatedAt = updatedAt,
    status = status
)