package ir.amirroid.simplechat.data.database.entities

import androidx.room.Entity
import ir.amirroid.simplechat.models.room.MemberRole

@Entity("members", primaryKeys = ["userId", "roomId"])
data class RoomMemberEntity(
    val isMe: Boolean,
    val role: MemberRole,
    val userId: String,
    val roomId: Long
)
