package ir.amirroid.simplechat.models.message

import ir.amirroid.simplechat.models.user.User
import kotlinx.serialization.Serializable

@Serializable
data class MessageStatus(
    val messageId: Long,
    val user: User,
    val status: MessageDeliveryStatus
)