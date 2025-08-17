package ir.amirroid.simplechat.stream

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import io.ktor.http.HttpHeaders
import ir.amirroid.simplechat.auth.manager.AuthenticationManager
import ir.amirroid.simplechat.data.models.body.SendMessageBody
import ir.amirroid.simplechat.data.models.user.User
import ir.amirroid.simplechat.extensions.addEventListener
import ir.amirroid.simplechat.socket.BaseSocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class StreamSocketManagerImpl(
    private val socketManager: BaseSocketManager,
    private val authenticationManager: AuthenticationManager,
    private val json: Json
) : StreamSocketManager {
    private val clients = ConcurrentHashMap<String, SocketIOClient>()
    private val users = ConcurrentHashMap<SocketIOClient, User>()

    private val server: SocketIOServer
        get() = socketManager.server

    private val scope: CoroutineScope
        get() = socketManager.coroutineScop


    private fun registerClient(
        user: User,
        client: SocketIOClient
    ) {
        clients[user.userId] = client
        users[client] = user
    }

    private fun unregisterClient(userId: String) {
        clients.remove(userId)
        users.entries.find { it.value.userId == userId }?.key?.also {
            users.remove(it)
        }
    }


    override fun startListening() {
        addJoinEventListener()
        addDisconnectEventListener()
        addSendMessageEventListener()
    }

    private fun addJoinEventListener() {
        server.addEventListener("join", String::class.java) { client, _, _ ->
            val token = client.handshakeData.httpHeaders[HttpHeaders.Authorization]
            val principal = runBlocking {
                authenticationManager.getUserPrincipleFromToken(
                    token,
                    authenticationManager.generateCredentialFromToken(token)
                )
            }
            if (principal == null) {
                client.disconnect()
            } else {
                registerClient(principal.user, client)
            }
        }
    }

    private fun addDisconnectEventListener() {
        server.addDisconnectListener { client ->
            val userId = getUserFromClient(client)?.userId ?: return@addDisconnectListener
            unregisterClient(userId)
        }
    }

    private fun getUserFromClient(client: SocketIOClient): User? {
        return users.getOrDefault(client, null)
    }

    fun addSendMessageEventListener() {
        server.addEventListener<SendMessageBody>(
            eventName = "send_message",
            json = json
        ) { client, data, ackSender ->
            val user = getUserFromClient(client)
            client.sendEvent("send_message", "${user?.username} ${data.content}")
        }
    }
}