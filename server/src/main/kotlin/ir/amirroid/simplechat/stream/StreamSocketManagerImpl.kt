package ir.amirroid.simplechat.stream

import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import ir.amirroid.simplechat.data.models.user.User
import java.util.concurrent.ConcurrentHashMap

class StreamSocketManagerImpl : StreamSocketManager {
    private val clients = ConcurrentHashMap<String, WebSocketServerSession>()

    override fun registerClient(
        userId: String,
        session: WebSocketServerSession
    ) {
        clients[userId] = session
    }

    override fun unregisterClient(userId: String) {
        clients.remove(userId)
    }

    override suspend fun handleFrame(
        from: User,
        frame: Frame
    ) {
        when (frame) {
            is Frame.Close -> unregisterClient(from.userId)
            is Frame.Text -> handleTextFrame(from, frame)

            else -> Unit
        }
    }

    private suspend fun handleTextFrame(from: User, frame: Frame.Text) {
        val text = frame.readText()
        clients.filter { (userId, _) -> userId != from.userId }.forEach { (_, session) ->
            session.send("(${from.username}) $text")
        }
    }
}