package ir.amirroid.simplechat.socket

import com.corundumstudio.socketio.SocketIOServer
import kotlinx.coroutines.CoroutineScope

interface BaseSocketManager {
    val server: SocketIOServer
    val coroutineScop: CoroutineScope

    fun startServer()
    fun stopServer()
}