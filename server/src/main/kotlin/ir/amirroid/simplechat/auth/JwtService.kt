package ir.amirroid.simplechat.auth

import com.auth0.jwt.JWTVerifier
import ir.amirroid.simplechat.data.models.token.Token

interface JwtService {
    fun generateToken(userId: String): Token
    fun getVerifier(): JWTVerifier
}