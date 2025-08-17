package ir.amirroid.simplechat.stream

import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.websocket.Frame
import ir.amirroid.simplechat.data.models.user.User

interface StreamSocketManager {
    fun registerClient(userId: String, session: WebSocketServerSession)
    fun unregisterClient(userId: String)
    suspend fun handleFrame(from: User, frame: Frame)
}