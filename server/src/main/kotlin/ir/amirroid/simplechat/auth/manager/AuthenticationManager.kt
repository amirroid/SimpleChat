package ir.amirroid.simplechat.auth.manager

import io.ktor.server.auth.jwt.JWTCredential
import ir.amirroid.simplechat.auth.UserPrincipal

interface AuthenticationManager {
    suspend fun getUserPrincipleFromToken(
        token: String, credential: JWTCredential
    ): UserPrincipal?

    fun generateCredentialFromToken(token: String): JWTCredential
}