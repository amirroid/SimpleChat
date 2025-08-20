package ir.amirroid.simplechat.models.body

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomBody(
    val userId: String
)