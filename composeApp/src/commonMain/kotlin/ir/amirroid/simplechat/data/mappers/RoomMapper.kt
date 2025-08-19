package ir.amirroid.simplechat.data.mappers

import ir.amirroid.simplechat.data.database.entities.RoomEntity
import ir.amirroid.simplechat.models.room.Room

fun Room.toEntity(): RoomEntity =
    RoomEntity(
        id = id,
        createdAt = createdAt,
        lastMessage = lastMessage,
        isPrivate = isPrivate,
        name = name
    )