package ir.amirroid.simplechat.data.call

import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.data.response.onError
import ir.amirroid.simplechat.data.response.onSuccess
import kotlinx.coroutines.delay

object RetryCallExecutor {
    suspend fun <D> call(
        times: Int = 3,
        eachDelay: Long = 2000,
        block: suspend () -> Response<D, NetworkErrors>
    ): Response<D, NetworkErrors> {
        var currentAttempt = 0
        var lastData: Response<D, NetworkErrors> = Response.Loading

        while (currentAttempt < times) {
            val data = block()
            lastData = data.onSuccess {
                return Response.Success(it)
            }.onError {
                if (it is NetworkErrors.Unauthorized) {
                    return Response.Error(it)
                }
                currentAttempt++
            }
            if (eachDelay != 0L) delay(eachDelay)
        }

        return lastData
    }
}