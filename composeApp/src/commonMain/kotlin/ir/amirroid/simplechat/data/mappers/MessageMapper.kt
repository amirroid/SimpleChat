package ir.amirroid.simplechat.data.mappers

import ir.amirroid.simplechat.data.database.entities.MessageEntity
import ir.amirroid.simplechat.models.message.Message

fun Message.toEntity() = MessageEntity(
    id = id,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    senderUserId = sender.user.userId,
    roomId = roomId
)