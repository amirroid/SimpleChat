package ir.amirroid.simplechat.socket

import com.corundumstudio.socketio.SocketIOServer
import ir.amirroid.simplechat.extensions.addEventListener
import ir.amirroid.simplechat.socket.events.SocketEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

interface BaseSocketManager {
    val server: SocketIOServer
    val coroutineScop: CoroutineScope
    val json: Json

    fun startServer()
    fun stopServer()

}

inline fun <reified T> BaseSocketManager.addEventListener(socketEventListener: SocketEventListener<T>) {
    socketEventListener.socketManager = this
    if (T::class.qualifiedName in listOf(String::class.qualifiedName)) {
        server.addEventListener(
            socketEventListener.eventName,
            String::class.java,
        ) { client, data, ackSender ->
            socketEventListener.handleEventListener(client, data as T, ackSender)
        }
    } else {
        server.addEventListener<T>(
            eventName = socketEventListener.eventName,
            json = json,
            listener = socketEventListener::handleEventListener
        )
    }
}