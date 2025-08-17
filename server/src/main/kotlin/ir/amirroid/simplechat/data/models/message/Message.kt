package ir.amirroid.simplechat.data.models.message

import ir.amirroid.simplechat.data.models.room.RoomMember
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val sender: RoomMember,
    val roomId: Long
)