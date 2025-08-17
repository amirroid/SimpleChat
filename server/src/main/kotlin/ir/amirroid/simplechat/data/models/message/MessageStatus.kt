package ir.amirroid.simplechat.data.models.message

import ir.amirroid.simplechat.data.models.user.User
import ir.amirroid.simplechat.database.message_status.MessageStatuses
import kotlinx.serialization.Serializable

@Serializable
data class MessageStatus(
    val messageId: Long,
    val user: User,
    val status: MessageStatuses
)