package ir.amirroid.simplechat.plugins

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.getAs
import ir.amirroid.simplechat.auth.jwt.JwtService
import ir.amirroid.simplechat.auth.manager.AuthenticationManager
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val jwtService by inject<JwtService>()
    val authenticationManager: AuthenticationManager by inject()
    val realm = environment.config.property("jwt.realm").getAs<String>()

    install(Authentication) {
        jwt {
            this.realm = realm
            verifier(jwtService.getVerifier())

            validate { credential ->
                val headerToken = request.headers[HttpHeaders.Authorization] ?: return@validate null
                authenticationManager.getUserPrincipleFromToken(headerToken, credential)
            }
        }
    }
}