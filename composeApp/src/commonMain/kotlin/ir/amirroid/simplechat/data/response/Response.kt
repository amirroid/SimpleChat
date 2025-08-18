package ir.amirroid.simplechat.data.response

import ir.amirroid.simplechat.utils.ErrorI

sealed interface Response<out D, out E : ErrorI> {
    data class Success<D>(val data: D) : Response<D, Nothing>
    data class Error<E : ErrorI>(val error: E) : Response<Nothing, E>
    data object Loading : Response<Nothing, Nothing>
    data object Idle : Response<Nothing, Nothing>
}