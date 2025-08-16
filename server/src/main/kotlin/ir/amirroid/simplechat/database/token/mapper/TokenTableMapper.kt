package ir.amirroid.simplechat.database.token.mapper

import ir.amirroid.simplechat.data.models.token.Token
import ir.amirroid.simplechat.database.token.TokenTable
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toToken() = Token(
    token = this[TokenTable.token],
    expiresAt = this[TokenTable.expiresAt],
    createdAt = this[TokenTable.createdAt],
)