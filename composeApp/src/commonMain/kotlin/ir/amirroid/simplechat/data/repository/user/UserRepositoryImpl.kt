package ir.amirroid.simplechat.data.repository.user

import io.github.xxfast.kstore.KStore
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ir.amirroid.simplechat.data.call.SafeApiCall
import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.token.Token
import ir.amirroid.simplechat.models.user.RegisterUserBody

class UserRepositoryImpl(
    private val tokenStore: KStore<Token>,
    private val httpClient: HttpClient
) : UserRepository {
    override val user = tokenStore.updates
    override suspend fun login(body: RegisterUserBody): Response<DefaultRequiredResponse<Token>, NetworkErrors> {
        return SafeApiCall.launch {
            httpClient.post("login") {
                setBody(body)
            }
        }
    }

    override suspend fun signup(body: RegisterUserBody): Response<DefaultRequiredResponse<Token>, NetworkErrors> {
        return SafeApiCall.launch {
            httpClient.post("signup") {
                setBody(body)
            }
        }
    }

    override suspend fun saveToken(token: Token) {
        tokenStore.set(token)
    }
}