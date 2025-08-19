package ir.amirroid.simplechat.data.mappers

import ir.amirroid.simplechat.data.database.entities.RoomMemberEntity
import ir.amirroid.simplechat.models.room.RoomMember
import ir.amirroid.simplechat.models.user.User

fun RoomMemberEntity.toRoomMember(user: User) = RoomMember(
    isMe = isMe,
    role = role,
    user = user
)