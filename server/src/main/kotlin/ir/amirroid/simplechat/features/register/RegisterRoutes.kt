package ir.amirroid.simplechat.features.register

import io.ktor.server.routing.Route
import ir.amirroid.simplechat.auth.jwt.JwtService
import ir.amirroid.simplechat.database.token.service.TokenService
import ir.amirroid.simplechat.database.user.service.UserService
import org.koin.ktor.ext.inject

fun Route.registerRoutes() {
    val userService by inject<UserService>()
    val tokenService by inject<TokenService>()
    val jwtService by inject<JwtService>()

    loginRoute(userService = userService, tokenService = tokenService, jwtService = jwtService)
    signUpRoute(userService = userService, tokenService = tokenService, jwtService = jwtService)
}