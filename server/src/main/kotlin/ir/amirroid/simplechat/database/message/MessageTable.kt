package ir.amirroid.simplechat.database.message

import ir.amirroid.simplechat.database.room.RoomTable
import ir.amirroid.simplechat.database.user.UserTable
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object MessageTable : LongIdTable("messages") {
    val content = text("content")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").nullable().default(null)
    val senderUserId = reference("sender_user_id", UserTable.userId, ReferenceOption.CASCADE)
    val roomId = reference("room_id", RoomTable.id, ReferenceOption.CASCADE)
}