package ir.amirroid.simplechat.auth.manager

import com.auth0.jwt.JWT
import io.ktor.server.auth.jwt.JWTCredential
import ir.amirroid.simplechat.auth.UserPrincipal
import ir.amirroid.simplechat.database.token.service.TokenService
import ir.amirroid.simplechat.database.user.service.UserService

class AuthenticationManagerImpl(
    private val userService: UserService,
    private val tokenService: TokenService,
) : AuthenticationManager {
    override suspend fun getUserPrincipleFromToken(
        token: String,
        credential: JWTCredential
    ): UserPrincipal? {
        val userId = credential.getClaim("user_id", String::class) ?: return null
        val userToken = tokenService.getUserToken(userId) ?: return null
        val headerToken = removeTokenPrefix(token)
        return if (userToken.token == headerToken) {
            UserPrincipal(userService.get(userId))
        } else null
    }

    override fun generateCredentialFromToken(token: String): JWTCredential {
        val decoded = JWT.decode(removeTokenPrefix(token))
        return JWTCredential(decoded)
    }

    private fun removeTokenPrefix(token: String) = token.removePrefix("Bearer ")?.trim()
}