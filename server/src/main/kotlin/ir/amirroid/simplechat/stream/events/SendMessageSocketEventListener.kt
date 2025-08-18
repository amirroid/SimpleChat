package ir.amirroid.simplechat.stream.events

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import ir.amirroid.simplechat.database.message.services.MessagesService
import ir.amirroid.simplechat.database.message_status.service.MessageStatusService
import ir.amirroid.simplechat.database.message_status.service.UserWithStatus
import ir.amirroid.simplechat.database.room_member.service.RoomMemberService
import ir.amirroid.simplechat.extensions.without
import ir.amirroid.simplechat.models.body.SendMessageBody
import ir.amirroid.simplechat.models.message.Message
import ir.amirroid.simplechat.models.message.MessageDeliveryStatus
import ir.amirroid.simplechat.models.user.User
import ir.amirroid.simplechat.socket.events.SocketEventListener
import ir.amirroid.simplechat.stream.StreamSocketManagerImpl
import ir.amirroid.simplechat.utils.SocketEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class SendMessageSocketEventListener(
    private val streamSocketManager: StreamSocketManagerImpl,
    private val messagesService: MessagesService,
    private val messageStatusService: MessageStatusService,
    private val roomMemberService: RoomMemberService,
    private val json: Json
) :
    SocketEventListener<SendMessageBody>(SocketEvents.SEND_MESSAGE) {
    private val scope get() = socketManager.coroutineScop
    val clients get() = streamSocketManager.clients

    override fun handleEventListener(
        client: SocketIOClient,
        data: SendMessageBody,
        ackSender: AckRequest
    ) {
        val user = streamSocketManager.getUserFromClient(client) ?: return

        sendMessage(user, data)
    }

    private fun sendMessage(user: User, body: SendMessageBody) =
        scope.launch(Dispatchers.IO) {
            val activeRoomClients = roomMemberService.getAllRoomMemberIds(body.roomId)
                .mapNotNull { clients[it] }
            val message = saveMessage(user, body)
            val messageId = message.id
            val myClient = this@SendMessageSocketEventListener.clients[user.userId]!!
            val usersWithStatus = buildUsersStatus(activeRoomClients.without(myClient))
            upsertStatuses(message.id, usersWithStatus)
            val newStatuses = messageStatusService.getMessageStatuses(messageId)
            val messageWithStatuses = message.copy(statuses = newStatuses)
            broadcast(activeRoomClients, messageWithStatuses, myClient)
        }

    private fun buildUsersStatus(clients: List<SocketIOClient>): List<UserWithStatus> =
        clients.mapNotNull { client ->
            streamSocketManager.users[client]?.let {
                UserWithStatus(
                    it.userId,
                    MessageDeliveryStatus.DELIVERED
                )
            }
        }

    private suspend fun upsertStatuses(messageId: Long, statuses: List<UserWithStatus>) =
        messageStatusService.upsertStatuses(messageId, statuses)


    private fun broadcast(
        clients: List<SocketIOClient>,
        message: Message,
        senderClient: SocketIOClient,
        eventName: String = SocketEvents.SEND_MESSAGE
    ) {
        val messageForOthers = message.copy(sender = message.sender.copy(isMe = false))
        val jsonForSender = json.encodeToString(message)
        val jsonForOthers = json.encodeToString(messageForOthers)

        clients.without(senderClient).forEach { it.sendEvent(eventName, jsonForOthers) }
        senderClient.sendEvent(eventName, jsonForSender)
    }


    private suspend fun saveMessage(
        user: User, body: SendMessageBody
    ) = messagesService.addMessage(body.content, roomId = body.roomId, userId = user.userId)
}