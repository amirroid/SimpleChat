package ir.amirroid.simplechat.data.database.entities

import androidx.room.Entity
import ir.amirroid.simplechat.models.message.MessageDeliveryStatus
import kotlinx.datetime.LocalDateTime

@Entity("status", primaryKeys = ["messageId", "userId"])
data class MessageStatusEntity(
    val messageId: Long,
    val userId: String,
    val updatedAt: LocalDateTime,
    val status: MessageDeliveryStatus
)