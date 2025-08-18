package ir.amirroid.simplechat.database.token.service

import ir.amirroid.simplechat.models.token.Token
import ir.amirroid.simplechat.database.token.TokenTable
import ir.amirroid.simplechat.database.token.mapper.toToken
import ir.amirroid.simplechat.exceptions.internalServerError
import ir.amirroid.simplechat.utils.dbQuery
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import java.time.LocalDateTime

class TokenServiceImpl(database: Database) : TokenService {
    override suspend fun getUserToken(userId: String): Token? = dbQuery {
        val now = LocalDateTime.now().toKotlinLocalDateTime()
        TokenTable
            .selectAll()
            .where { (TokenTable.userId eq userId) and (TokenTable.expiresAt greater now) }
            .singleOrNull()
            ?.toToken()
    }

    override suspend fun saveToken(
        userId: String,
        tokenObj: Token
    ) = dbQuery {
        val result = TokenTable.upsert {
            it[TokenTable.token] = tokenObj.token
            it[TokenTable.userId] = userId
            it[TokenTable.expiresAt] = tokenObj.expiresAt
        }.resultedValues?.firstOrNull() ?: internalServerError("Failed to save token")

        result.toToken()
    }


    init {
        transaction(database) {
            SchemaUtils.create(TokenTable)
        }
    }
}