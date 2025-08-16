package ir.amirroid.simplechat.database.room.mapper

import ir.amirroid.simplechat.data.models.room.RoomMember
import ir.amirroid.simplechat.database.room.RoomMemberTable
import ir.amirroid.simplechat.database.user.mapper.toUser
import org.jetbrains.exposed.v1.core.Alias
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toRoomMember(allMembers: Alias<RoomMemberTable>, myUserId: String): RoomMember {
    val userId = this[allMembers[RoomMemberTable.userId]]
    return RoomMember(
        isMe = userId == myUserId,
        role = this[allMembers[RoomMemberTable.role]],
        user = toUser()
    )
}