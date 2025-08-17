package ir.amirroid.simplechat.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeenMessageResponse(
    @SerialName("message_id")
    val messageId: Long,
    @SerialName("user_id")
    val userId: String
)
