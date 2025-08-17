package ir.amirroid.simplechat.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import ir.amirroid.simplechat.features.stream.streamSocket
import ir.amirroid.simplechat.socket.BaseSocketManager
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject

fun Application.configureSocketIO() {
    val socketManager: BaseSocketManager by inject()

    socketManager.startServer()

    streamSocket()

    monitor.subscribe(ApplicationStopping) {
        runBlocking { socketManager.stopServer() }
    }
}