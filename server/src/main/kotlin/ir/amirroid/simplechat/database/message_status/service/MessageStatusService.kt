package ir.amirroid.simplechat.database.message_status.service

import ir.amirroid.simplechat.models.message.MessageDeliveryStatus
import ir.amirroid.simplechat.models.message.MessageStatus

data class UserWithStatus(val userId: String, val status: MessageDeliveryStatus)

interface MessageStatusService {
    suspend fun upsertStatus(
        messageId: Long, userId: String, status: MessageDeliveryStatus
    )

    suspend fun upsertStatuses(
        messageId: Long, userWithStatuses: List<UserWithStatus>
    )

    suspend fun getMessageStatuses(messageId: Long): List<MessageStatus>
}