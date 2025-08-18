package ir.amirroid.simplechat.models

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse<T>(
    val ok: Boolean,
    val message: String?,
    val data: T? = null
)

@Serializable
data class DefaultRequiredResponse<T>(
    val ok: Boolean,
    val message: String?,
    val data: T
)

typealias ErrorResponse = DefaultResponse<String>