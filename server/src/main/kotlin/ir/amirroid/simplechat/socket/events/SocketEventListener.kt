package ir.amirroid.simplechat.socket.events

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import ir.amirroid.simplechat.socket.BaseSocketManager

abstract class SocketEventListener<T>(val eventName: String) {
    lateinit var socketManager: BaseSocketManager

    abstract fun handleEventListener(
        client: SocketIOClient, data: T, ackSender: AckRequest
    )
}