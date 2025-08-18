package ir.amirroid.simplechat.extensions

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import ir.amirroid.simplechat.models.DefaultResponse

suspend inline fun <reified T : Any> ApplicationCall.respondDefault(
    status: HttpStatusCode = HttpStatusCode.OK,
    message: String? = null,
    data: T? = null
) {
    respond(
        status = status,
        message = DefaultResponse(
            ok = status.value in 200..299,
            message = message,
            data = data
        )
    )
}

suspend fun ApplicationCall.respondDefault(
    status: HttpStatusCode = HttpStatusCode.OK,
    message: String? = null,
) {
    respondDefault<Unit>(
        status = status,
        message = message,
        data = null
    )
}