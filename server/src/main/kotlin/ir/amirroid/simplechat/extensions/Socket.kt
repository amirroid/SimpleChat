package ir.amirroid.simplechat.extensions

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.listener.DataListener
import kotlinx.serialization.json.Json

inline fun <reified T> SocketIOServer.addEventListener(
    eventName: String,
    json: Json,
    listener: DataListener<T>
) {
    addEventListener(
        eventName, String::class.java
    ) { client, data, ackSender ->
        runCatching { json.decodeFromString<T>(data) }.onSuccess {
            listener.onData(client, it, ackSender)
        }.onFailure {
            println("Failed to parse message: ${it.message}")
        }
    }
}

inline fun <reified T> SocketIOClient.sendEvent(
    eventName: String,
    json: Json,
    data: T
) {
    sendEvent(eventName, json.encodeToString(data))
}