package ir.amirroid.simplechat.data.mappers

import ir.amirroid.simplechat.data.database.entities.UserEntity
import ir.amirroid.simplechat.models.user.User

fun UserEntity.toUser() = User(
    userId = userId,
    username = username,
    createdAt = createdAt,
    updatedAt = updatedAt
)