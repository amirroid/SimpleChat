package ir.amirroid.simplechat.data.mappers

import ir.amirroid.simplechat.data.database.relations.RoomWithMembers
import ir.amirroid.simplechat.models.room.Room

fun RoomWithMembers.toRoom(): Room {
    return Room(
        id = room.id,
        name = room.name,
        isPrivate = room.isPrivate,
        createdAt = room.createdAt,
        members = members.map {
            it.member.toRoomMember(it.user.toUser())
        },
        lastMessage = room.lastMessage,
    )
}