package ir.amirroid.simplechat.data.models.response

import ir.amirroid.simplechat.models.ErrorResponse
import ir.amirroid.simplechat.utils.ErrorI

sealed class NetworkErrors(val response: ErrorResponse?) : ErrorI {
    data object RequestTimeout : NetworkErrors(null)
    data object Unauthorized : NetworkErrors(null)
    data class NotFound(val errorResponse: ErrorResponse?) : NetworkErrors(errorResponse)
    data object Serialize : NetworkErrors(null)
    data object NoInternet : NetworkErrors(null)
    data class Unknown(val errorResponse: ErrorResponse?) : NetworkErrors(errorResponse)
    data class ServerError(val errorResponse: ErrorResponse?) : NetworkErrors(errorResponse)
    data object ClientError : NetworkErrors(null)
    data class BadRequest(val errorResponse: ErrorResponse?) : NetworkErrors(errorResponse)
}