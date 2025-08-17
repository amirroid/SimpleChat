package ir.amirroid.simplechat.stream.events

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import ir.amirroid.simplechat.data.models.body.SeenMessage
import ir.amirroid.simplechat.database.message_status.MessageStatuses
import ir.amirroid.simplechat.database.message_status.service.MessageStatusService
import ir.amirroid.simplechat.database.room_member.service.RoomMemberService
import ir.amirroid.simplechat.extensions.without
import ir.amirroid.simplechat.socket.events.SocketEventListener
import ir.amirroid.simplechat.stream.StreamSocketManagerImpl
import ir.amirroid.simplechat.utils.SocketEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SeenSocketEventListener(
    private val streamSocketManager: StreamSocketManagerImpl,
    private val messageStatusService: MessageStatusService,
    private val roomMemberService: RoomMemberService
) :
    SocketEventListener<SeenMessage>(SocketEvents.SEEN) {
    override fun handleEventListener(
        client: SocketIOClient,
        data: SeenMessage,
        ackSender: AckRequest
    ) {
        val user = streamSocketManager.getUserFromClient(client) ?: return

        socketManager.coroutineScop.launch(Dispatchers.IO) {
            markMessageAsSeen(data.messageId, user.userId)
            broadcastSeenStatus(data, excludingUserId = user.userId)
        }
    }

    private suspend fun markMessageAsSeen(messageId: Long, userId: String) =
        messageStatusService.upsertStatus(messageId, userId, MessageStatuses.SEEN)

    private suspend fun broadcastSeenStatus(data: SeenMessage, excludingUserId: String) {
        val activeRoomClients = roomMemberService.getAllRoomMemberIdsFromMessage(data.messageId)
            .mapNotNull { streamSocketManager.clients[it] }
        activeRoomClients.without(streamSocketManager.clients[excludingUserId]!!)
            .forEach { client ->
                client.sendEvent(SocketEvents.SEEN, data)
            }
    }
}