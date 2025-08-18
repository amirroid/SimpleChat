package ir.amirroid.simplechat.models.user

import ir.amirroid.simplechat.serializers.SerializedLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id")
    val userId: String,
    val username: String,
    @SerialName("created_at")
    val createdAt: SerializedLocalDateTime? = null,
    @SerialName("updated_at")
    val updatedAt: SerializedLocalDateTime? = null
)