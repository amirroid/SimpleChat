package ir.amirroid.simplechat.plugins

import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebSockets() {
    val json: Json by inject()

    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(json)
        pingPeriod = 3.seconds
        timeout = 10.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}