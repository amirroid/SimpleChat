package ir.amirroid.simplechat.models.room

import ir.amirroid.simplechat.serializers.SerializedLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: Long,
    val name: String?,
    @SerialName("is_private")
    val isPrivate: Boolean,
    @SerialName("created_at")
    val createdAt: SerializedLocalDateTime,
    val members: List<RoomMember>,
    val lastMessage: String? = null
)