package ir.amirroid.simplechat.data.repository.user

import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.token.Token
import ir.amirroid.simplechat.models.user.RegisterUserBody
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val user: Flow<Token?>
    suspend fun login(body: RegisterUserBody): Response<DefaultRequiredResponse<Token>, NetworkErrors>
    suspend fun signup(body: RegisterUserBody): Response<DefaultRequiredResponse<Token>, NetworkErrors>
    suspend fun saveToken(token: Token)
}