package ir.amirroid.simplechat.data.mappers

import ir.amirroid.simplechat.data.database.entities.RoomMemberEntity
import ir.amirroid.simplechat.models.room.RoomMember

fun RoomMember.toEntity(roomId: Long): RoomMemberEntity =
    RoomMemberEntity(
        isMe = isMe,
        role = role,
        userId = user.userId,
        roomId = roomId
    )