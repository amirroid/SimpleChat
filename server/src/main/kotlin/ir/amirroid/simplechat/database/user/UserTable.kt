package ir.amirroid.simplechat.database.user

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object UserTable : Table("users") {
    val userId = varchar("user_id", 150)
    val username = varchar("username", 150)
    val password = text("password")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").nullable().default(null)

    override val primaryKey = PrimaryKey(userId)
}