package ir.amirroid.simplechat.features.stream

import io.ktor.server.application.Application
import ir.amirroid.simplechat.stream.StreamSocketManager
import org.koin.ktor.ext.inject

fun Application.streamSocket() {
    val streamManager: StreamSocketManager by inject()

    streamManager.startListening()
}