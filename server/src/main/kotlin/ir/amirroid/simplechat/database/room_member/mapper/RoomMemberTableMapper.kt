package ir.amirroid.simplechat.database.room_member.mapper

import ir.amirroid.simplechat.models.room.RoomMember
import ir.amirroid.simplechat.database.room_member.RoomMemberTable
import ir.amirroid.simplechat.database.user.UserTable
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

fun ResultRow.toRoomMember(
    allMembers: Alias<RoomMemberTable>,
    senderAlias: Alias<UserTable>,
    myUserId: String
): RoomMember {
    val userId = this[allMembers[RoomMemberTable.userId]]
    return RoomMember(
        isMe = userId == myUserId,
        role = this[allMembers[RoomMemberTable.role]],
        user = toUser(senderAlias)
    )
}

fun ResultRow.toRoomMember(myUserId: String): RoomMember {
    val userId = this[UserTable.userId]
    return RoomMember(
        isMe = myUserId == userId,
        role = this[RoomMemberTable.role],
        user = toUser()
    )
}