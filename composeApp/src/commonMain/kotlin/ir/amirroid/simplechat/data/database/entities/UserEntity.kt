package ir.amirroid.simplechat.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity("users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val username: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)