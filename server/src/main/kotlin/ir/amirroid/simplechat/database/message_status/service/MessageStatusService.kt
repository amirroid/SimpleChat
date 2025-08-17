package ir.amirroid.simplechat.database.message_status.service

import ir.amirroid.simplechat.data.models.message.MessageStatus
import ir.amirroid.simplechat.database.message_status.MessageStatuses

data class UserWithStatus(val userId: String, val status: MessageStatuses)

interface MessageStatusService {
    suspend fun upsertStatus(
        messageId: Long, userId: String, status: MessageStatuses
    )

    suspend fun upsertStatuses(
        messageId: Long, userWithStatuses: List<UserWithStatus>
    )

    suspend fun getMessageStatuses(messageId: Long): List<MessageStatus>
}