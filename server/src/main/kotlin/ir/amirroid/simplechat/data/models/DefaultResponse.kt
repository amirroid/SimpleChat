package ir.amirroid.simplechat.data.models

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse<T>(
    val ok: Boolean,
    val message: String?,
    val data: T? = null
)