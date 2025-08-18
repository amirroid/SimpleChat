package ir.amirroid.simplechat.features.register

import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import ir.amirroid.simplechat.auth.jwt.JwtService
import ir.amirroid.simplechat.models.user.RegisterUserBody
import ir.amirroid.simplechat.database.token.service.TokenService
import ir.amirroid.simplechat.database.user.service.UserService
import ir.amirroid.simplechat.exceptions.badRequestError
import ir.amirroid.simplechat.exceptions.unauthorizedError
import ir.amirroid.simplechat.extensions.respondDefault

fun Route.loginRoute(
    userService: UserService,
    tokenService: TokenService,
    jwtService: JwtService
) {
    post("/login") {
        val body = call.receive<RegisterUserBody>()
        val userId = body.userId

        val isValid = userService.verifyPassword(userId, body.password)
        if (!isValid) {
            badRequestError("Invalid credentials")
        }

        val token = tokenService.getUserToken(userId)
            ?: tokenService.saveToken(userId, jwtService.generateToken(userId))

        call.respondDefault(
            data = token
        )
    }
}