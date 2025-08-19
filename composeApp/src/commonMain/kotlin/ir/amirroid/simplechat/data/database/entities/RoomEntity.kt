package ir.amirroid.simplechat.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity("rooms")
data class RoomEntity(
    @PrimaryKey val id: Long,
    val name: String?,
    val createdAt: LocalDateTime,
    val lastMessage: String?,
    val isPrivate: Boolean
)