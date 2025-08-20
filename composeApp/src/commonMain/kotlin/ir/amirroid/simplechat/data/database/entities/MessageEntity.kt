package ir.amirroid.simplechat.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.amirroid.simplechat.serializers.SerializedLocalDateTime
import kotlinx.datetime.LocalDateTime

@Entity("messages")
data class MessageEntity(
    @PrimaryKey val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: SerializedLocalDateTime?,
    val senderUserId: String,
    val roomId: Long,
)
