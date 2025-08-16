package ir.amirroid.simplechat.data.models.user

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id")
    val userId: String,
    val username: String,
    @SerialName("created_at")
    val createdAt: LocalDateTime? = null,
    @SerialName("updated_at")
    val updatedAt: LocalDateTime? = null
)