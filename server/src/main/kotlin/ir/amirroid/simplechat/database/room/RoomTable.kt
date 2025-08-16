package ir.amirroid.simplechat.database.room

import ir.amirroid.simplechat.database.user.UserTable
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object RoomTable : LongIdTable("rooms") {
    val name = varchar("name", 150).nullable().default(null)
    val isPrivate = bool("is_private").default(false)
    val createdBy = reference("created_by", UserTable.userId, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").nullable().default(null)
}