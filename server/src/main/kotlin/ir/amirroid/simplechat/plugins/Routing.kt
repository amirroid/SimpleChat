package ir.amirroid.simplechat.plugins

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import ir.amirroid.simplechat.auth.UserPrincipal
import ir.amirroid.simplechat.extensions.respondDefault
import ir.amirroid.simplechat.features.register.registerRoutes

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
        get {
            val userPrincipal = call.principal<UserPrincipal>()!!
            call.respondDefault(data = userPrincipal.user)
        }
    }
}