package ir.amirroid.simplechat.database.token

import ir.amirroid.simplechat.database.user.UserTable
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object TokenTable : Table("tokens") {
    val userId = varchar("user_id", 150)
        .references(UserTable.userId, onDelete = ReferenceOption.CASCADE)
    val token = text("token")
    val expiresAt = datetime("expires_at")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(userId)
}