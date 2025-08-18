package ir.amirroid.simplechat.stream

import com.corundumstudio.socketio.SocketIOClient
import ir.amirroid.simplechat.auth.manager.AuthenticationManager
import ir.amirroid.simplechat.models.user.User
import ir.amirroid.simplechat.database.message.services.MessagesService
import ir.amirroid.simplechat.database.message_status.service.MessageStatusService
import ir.amirroid.simplechat.database.room_member.service.RoomMemberService
import ir.amirroid.simplechat.socket.BaseSocketManager
import ir.amirroid.simplechat.socket.addEventListener
import ir.amirroid.simplechat.stream.events.JoinSocketEventListener
import ir.amirroid.simplechat.stream.events.SeenSocketEventListener
import ir.amirroid.simplechat.stream.events.SendMessageSocketEventListener
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class StreamSocketManagerImpl(
    private val socketManager: BaseSocketManager,
    private val authenticationManager: AuthenticationManager,
    private val messagesService: MessagesService,
    private val messageStatusService: MessageStatusService,
    private val roomMemberService: RoomMemberService,
    private val json: Json
) : StreamSocketManager {
    val clients = ConcurrentHashMap<String, SocketIOClient>()
    val users = ConcurrentHashMap<SocketIOClient, User>()


    fun registerClient(
        user: User,
        client: SocketIOClient
    ) {
        clients[user.userId] = client
        users[client] = user
    }

    fun unregisterClient(userId: String) {
        clients.remove(userId)
        users.entries.find { it.value.userId == userId }?.key?.also {
            users.remove(it)
        }
    }


    override fun startListening() {
        addDisconnectEventListener()
        socketManager.addEventListener(JoinSocketEventListener(this, authenticationManager))
        socketManager.addEventListener(
            SeenSocketEventListener(
                this,
                messageStatusService,
                roomMemberService
            )
        )
        socketManager.addEventListener(
            SendMessageSocketEventListener(
                this,
                messagesService,
                messageStatusService,
                roomMemberService,
                json
            )
        )
    }

    private fun addDisconnectEventListener() {
        socketManager.server.addDisconnectListener { client ->
            val userId = getUserFromClient(client)?.userId ?: return@addDisconnectListener
            unregisterClient(userId)
        }
    }

    fun getUserFromClient(client: SocketIOClient): User? {
        return users.getOrDefault(client, null)
    }
}