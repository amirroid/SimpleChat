package ir.amirroid.simplechat.data.models.token

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val token: String,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime? = null
)
