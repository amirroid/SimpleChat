package ir.amirroid.simplechat.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun createPlatformClient(): HttpClient {
    return HttpClient(Darwin)
}