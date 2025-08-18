package ir.amirroid.simplechat.database.token.service

import ir.amirroid.simplechat.models.token.Token

interface TokenService {
    suspend fun getUserToken(userId: String): Token?
    suspend fun saveToken(userId: String, tokenObj: Token): Token
}