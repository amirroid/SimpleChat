package ir.amirroid.simplechat.models.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageBody(
    val content: String,
    @SerialName("room_id")
    val roomId: Long
)