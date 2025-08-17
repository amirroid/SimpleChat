package ir.amirroid.simplechat.data.models.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeenMessageBody(
    @SerialName("message_id")
    val messageId: Long
)
