package ir.amirroid.simplechat.database.user.mapper

import ir.amirroid.simplechat.data.models.user.User
import ir.amirroid.simplechat.database.user.UserTable
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toUser() = User(
    userId = this[UserTable.userId],
    username = this[UserTable.username],
    createdAt = this[UserTable.createdAt],
    updatedAt = this[UserTable.updatedAt],
)