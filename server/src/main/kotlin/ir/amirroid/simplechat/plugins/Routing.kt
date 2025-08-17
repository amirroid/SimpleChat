package ir.amirroid.simplechat.plugins

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import ir.amirroid.simplechat.features.messages.messagesRoute
import ir.amirroid.simplechat.features.register.registerRoutes
import ir.amirroid.simplechat.features.room.roomsRoute

fun Application.configureRouting() {
    routing {
        route("/api") {
            route("/v1") {
                handleRoutes()
            }
        }
    }
}


fun Route.handleRoutes() {
    registerRoutes()

    authenticate {
        route("/rooms") {
            roomsRoute()
            messagesRoute()
        }
    }
}