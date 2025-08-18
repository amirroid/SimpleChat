package ir.amirroid.simplechat.auth.jwt

import com.auth0.jwt.JWTVerifier
import ir.amirroid.simplechat.models.token.Token

interface JwtService {
    fun generateToken(userId: String): Token
    fun getVerifier(): JWTVerifier
}