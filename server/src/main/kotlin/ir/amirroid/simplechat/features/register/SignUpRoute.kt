package ir.amirroid.simplechat.features.register

import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import ir.amirroid.simplechat.auth.JwtService
import ir.amirroid.simplechat.data.models.user.RegisterUserBody
import ir.amirroid.simplechat.database.token.service.TokenService
import ir.amirroid.simplechat.database.user.service.UserService
import ir.amirroid.simplechat.exceptions.conflictError
import ir.amirroid.simplechat.extensions.respondDefault

fun Route.signUpRoute(
    userService: UserService,
    tokenService: TokenService,
    jwtService: JwtService
) {
    post("/signup") {
        val body = call.receive<RegisterUserBody>()
        val userId = body.userId

        if (userService.existsByUserId(userId)) {
            conflictError("User with id $userId already exists")
        }

        val user = userService.insert(body)
        val token = jwtService.generateToken(user.userId)
        val createdToken = tokenService.saveToken(user.userId, token)

        call.respondDefault(
            data = createdToken
        )
    }
}