package ir.amirroid.simplechat.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun createPlatformClient(): HttpClient {
    return HttpClient(OkHttp)
}