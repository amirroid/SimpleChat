package ir.amirroid.simplechat.models.room

import ir.amirroid.simplechat.models.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


enum class MemberRole {
    MEMBER, OWNER
}

@Serializable
data class RoomMember(
    @SerialName("is_me")
    val isMe: Boolean,
    val role: MemberRole,
    val user: User
)