package ir.amirroid.simplechat.data.models.room

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val name: String?,
    @SerialName("is_private")
    val isPrivate: Boolean,
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    val members: List<RoomMember>
)