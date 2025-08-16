package ir.amirroid.simplechat.data.models.room

import ir.amirroid.simplechat.data.models.user.User
import ir.amirroid.simplechat.database.room.MemberRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomMember(
    @SerialName("is_me")
    val isMe: Boolean,
    val role: MemberRole,
    val user: User
)