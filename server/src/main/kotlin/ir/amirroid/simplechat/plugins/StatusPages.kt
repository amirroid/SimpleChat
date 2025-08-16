package ir.amirroid.simplechat.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import ir.amirroid.simplechat.exceptions.HttpException
import ir.amirroid.simplechat.extensions.respondDefault

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<HttpException> { call, cause ->
            val status = HttpStatusCode.fromValue(cause.errorCode)
            call.respondDefault(
                status = status,
                message = cause.message
            )
        }
        exception<Exception> { call, cause ->
            call.respondDefault(
                status = HttpStatusCode.InternalServerError,
                message = cause.message
            )
            throw cause
        }


        listOf(
            HttpStatusCode.Unauthorized,
            HttpStatusCode.MethodNotAllowed,
        ).forEach { status ->
            status(status) {
                call.respondDefault(
                    status = status,
                    message = status.description
                )
            }
        }
    }
}