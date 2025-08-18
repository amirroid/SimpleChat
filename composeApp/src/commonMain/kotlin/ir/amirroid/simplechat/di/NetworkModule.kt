package ir.amirroid.simplechat.di

import io.github.xxfast.kstore.KStore
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import ir.amirroid.simplechat.data.repository.user.UserRepository
import ir.amirroid.simplechat.models.token.Token
import ir.amirroid.simplechat.utils.API_VERSION
import ir.amirroid.simplechat.utils.SCHEME
import ir.amirroid.simplechat.utils.SERVER_HOST
import ir.amirroid.simplechat.utils.SERVER_PORT
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

expect fun createPlatformClient(): HttpClient

val baseUrlQualifier = qualifier("base_url")

val networkModule = module {
    factory(baseUrlQualifier) { "${SCHEME}://${SERVER_HOST}:${SERVER_PORT}/api/v$API_VERSION/" }
    single<Logger> {
        object : Logger {
            override fun log(message: String) {
                co.touchlab.kermit.Logger.withTag("SimpleChatAPI").d { message }
            }
        }
    }
    single {
        val store: KStore<Token> = get()
        createPlatformClient().config {
            defaultRequest {
                url(get<String>(baseUrlQualifier))
                contentType(ContentType.Application.Json)
            }
            install(Logging) {
                level = LogLevel.BODY
                logger = get()
            }
            install(ContentNegotiation) {
                json(get())
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        store.get()?.token?.let {
                            BearerTokens(
                                it,
                                null
                            )
                        }
                    }
                }
            }
        }
    }
}