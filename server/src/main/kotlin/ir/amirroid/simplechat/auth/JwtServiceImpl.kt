package ir.amirroid.simplechat.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.config.getAs
import ir.amirroid.simplechat.data.models.token.Token
import ir.amirroid.simplechat.extensions.toKotlinLocalDateTime
import java.util.Date

class JwtServiceImpl(
    environment: ApplicationEnvironment
) : JwtService {
    private val audience = environment.config.property("jwt.audience").getAs<String>()
    private val issuer = environment.config.property("jwt.issuer").getAs<String>()
    private val secret = environment.config.property("jwt.secret").getAs<String>()
    private val expiration = environment.config.property("jwt.expiration").getAs<Long>()

    private val algorithm by lazy {
        Algorithm.HMAC256(secret)
    }

    override fun generateToken(
        userId: String
    ): Token {
        val expireTime = Date(System.currentTimeMillis() + expiration)
        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("user_id", userId)
            .withExpiresAt(expireTime)
            .sign(algorithm)

        return Token(
            token,
            expiresAt = expireTime.toKotlinLocalDateTime()
        )
    }

    override fun getVerifier(): JWTVerifier {
        return JWT.require(algorithm)
            .withIssuer(issuer)
            .withAudience(audience)
            .build()
    }
}