package ir.amirroid.simplechat.data.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserBody(
    @SerialName("user_id")
    val userId: String,
    val password: String,
    val username: String = ""
)