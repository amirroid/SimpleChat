package ir.amirroid.simplechat.database.user.mapper

import ir.amirroid.simplechat.models.user.User
import ir.amirroid.simplechat.database.user.UserTable
import org.jetbrains.exposed.v1.core.Alias
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toUser() = User(
    userId = this[UserTable.userId],
    username = this[UserTable.username],
    createdAt = this[UserTable.createdAt],
    updatedAt = this[UserTable.updatedAt],
)

fun ResultRow.toUser(alias: Alias<UserTable>) = User(
    userId = this[alias[UserTable.userId]],
    username = this[alias[UserTable.username]],
    createdAt = this[alias[UserTable.createdAt]],
    updatedAt = this[alias[UserTable.updatedAt]],
)