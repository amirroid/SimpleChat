package ir.amirroid.simplechat.models.message

import ir.amirroid.simplechat.models.room.RoomMember
import ir.amirroid.simplechat.serializers.SerializedLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Long,
    val content: String,
    val createdAt: SerializedLocalDateTime,
    val updatedAt: SerializedLocalDateTime?,
    val sender: RoomMember,
    val roomId: Long,
    val statuses: List<MessageStatus>
)