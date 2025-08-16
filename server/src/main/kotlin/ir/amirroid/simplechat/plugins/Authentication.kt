package ir.amirroid.simplechat.plugins

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.getAs
import ir.amirroid.simplechat.auth.JwtService
import ir.amirroid.simplechat.auth.UserPrincipal
import ir.amirroid.simplechat.database.token.service.TokenService
import ir.amirroid.simplechat.database.user.service.UserService
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val jwtService by inject<JwtService>()
    val tokenService by inject<TokenService>()
    val userService by inject<UserService>()
    val realm = environment.config.property("jwt.realm").getAs<String>()

    install(Authentication) {
        jwt {
            this.realm = realm
            verifier(jwtService.getVerifier())

            validate {
                val userId = it.getClaim("user_id", String::class) ?: return@validate null
                val userToken = tokenService.getUserToken(userId) ?: return@validate null
                val headerToken = request.headers[HttpHeaders.Authorization]
                    ?.removePrefix("Bearer ") ?: return@validate null
                if (userToken.token == headerToken) {
                    UserPrincipal(userService.get(userId))
                } else null
            }
        }
    }
}