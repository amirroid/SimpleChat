package ir.amirroid.simplechat.database.message_status

import ir.amirroid.simplechat.database.message.MessageTable
import ir.amirroid.simplechat.database.user.UserTable
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

enum class MessageStatuses { DELIVERED, SEEN }

object MessageStatusTable : Table("message_status") {
    val messageId = reference("message_id", MessageTable.id, ReferenceOption.CASCADE)
    val userId = reference("user_id", UserTable.userId, ReferenceOption.CASCADE)
    val status = enumeration("status", MessageStatuses::class)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(messageId, userId)
}