package ir.amirroid.simplechat.models.token

import ir.amirroid.simplechat.serializers.SerializedLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val token: String,
    val expiresAt: SerializedLocalDateTime,
    val createdAt: SerializedLocalDateTime? = null,
    val userId: String = ""
)
