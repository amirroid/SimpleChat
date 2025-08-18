package ir.amirroid.simplechat.data.call

import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.data.response.onError
import ir.amirroid.simplechat.data.response.onSuccess

object RetryCallExecutor {
    suspend fun <D> call(
        times: Int = 3,
        block: suspend () -> Response<D, NetworkErrors>
    ): Response<D, NetworkErrors> {
        var currentAttempt = 0
        var lastData: Response<D, NetworkErrors> = Response.Loading

        while (currentAttempt < times) {
            val data = block()
            lastData = data.onSuccess {
                return Response.Success(it)
            }.onError {
                currentAttempt++
            }
        }

        return lastData
    }
}