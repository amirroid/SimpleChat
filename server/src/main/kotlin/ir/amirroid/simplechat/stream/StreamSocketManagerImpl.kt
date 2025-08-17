package ir.amirroid.simplechat.stream

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import io.ktor.http.HttpHeaders
import ir.amirroid.simplechat.auth.manager.AuthenticationManager
import ir.amirroid.simplechat.data.models.body.SendMessageBody
import ir.amirroid.simplechat.data.models.message.Message
import ir.amirroid.simplechat.data.models.user.User
import ir.amirroid.simplechat.database.message.services.MessagesService
import ir.amirroid.simplechat.database.message_status.MessageStatuses
import ir.amirroid.simplechat.database.message_status.service.MessageStatusService
import ir.amirroid.simplechat.database.message_status.service.UserWithStatus
import ir.amirroid.simplechat.extensions.addEventListener
import ir.amirroid.simplechat.extensions.without
import ir.amirroid.simplechat.socket.BaseSocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class StreamSocketManagerImpl(
    private val socketManager: BaseSocketManager,
    private val authenticationManager: AuthenticationManager,
    private val messagesService: MessagesService,
    private val messageStatusService: MessageStatusService,
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
            val user = getUserFromClient(client) ?: return@addEventListener

            sendMessage(user, data, clients.values.toList())
        }
    }

    private fun sendMessage(user: User, body: SendMessageBody, clients: List<SocketIOClient>) =
        scope.launch(Dispatchers.IO) {
            val message = saveMessage(user, body)
            val messageId = message.id
            val myClient = this@StreamSocketManagerImpl.clients[user.userId]!!
            val usersWithStatus = buildUsersStatus(clients.without(myClient))
            upsertStatuses(message.id, usersWithStatus)
            val newStatuses = messageStatusService.getMessageStatuses(messageId)
            val messageWithStatuses = message.copy(statuses = newStatuses)
            broadcast(clients, messageWithStatuses, myClient)
        }

    private fun buildUsersStatus(clients: List<SocketIOClient>): List<UserWithStatus> =
        clients.mapNotNull { client ->
            users[client]?.let { UserWithStatus(it.userId, MessageStatuses.DELIVERED) }
        }

    private suspend fun upsertStatuses(messageId: Long, statuses: List<UserWithStatus>) =
        messageStatusService.upsertStatuses(messageId, statuses)


    private fun broadcast(
        clients: List<SocketIOClient>,
        message: Message,
        senderClient: SocketIOClient
    ) {
        val messageForOthers = message.copy(sender = message.sender.copy(isMe = false))
        val jsonForSender = json.encodeToString(message)
        val jsonForOthers = json.encodeToString(messageForOthers)

        clients.without(senderClient).forEach { it.sendEvent("send_message", jsonForOthers) }
        senderClient.sendEvent("send_message", jsonForSender)
    }

    private suspend fun saveMessage(
        user: User, body: SendMessageBody
    ) = messagesService.addMessage(body.content, roomId = body.roomId, userId = user.userId)

    fun Map<String, SocketIOClient>.except(userId: String): List<SocketIOClient> {
        return this.filter { (id, _) -> id != userId }
            .map { it.value }
    }
}