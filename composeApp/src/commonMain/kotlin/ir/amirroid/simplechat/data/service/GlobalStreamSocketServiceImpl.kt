package ir.amirroid.simplechat.data.service

import dev.icerock.moko.socket.Socket
import dev.icerock.moko.socket.SocketBuilder
import dev.icerock.moko.socket.SocketOptions
import ir.amirroid.simplechat.data.repository.chat.ChatRepository
import ir.amirroid.simplechat.data.repository.room.RoomRepository
import ir.amirroid.simplechat.data.repository.user.UserRepository
import ir.amirroid.simplechat.models.body.CreateRoomBody
import ir.amirroid.simplechat.models.body.SeenMessageBody
import ir.amirroid.simplechat.models.body.SendMessageBody
import ir.amirroid.simplechat.models.message.Message
import ir.amirroid.simplechat.models.response.SeenMessageResponse
import ir.amirroid.simplechat.models.room.Room
import ir.amirroid.simplechat.utils.SocketEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GlobalStreamSocketServiceImpl(
    private val socketUrl: String,
    private val userRepository: UserRepository,
    val json: Json,
    private val chatRepository: ChatRepository,
    private val roomRepository: RoomRepository
) : GlobalStreamSocketService {
    private var socket: Socket? = null

    val job = SupervisorJob()
    private val scope = CoroutineScope(job)

    override suspend fun connect() {
        userRepository.user.collect { user ->
            user ?: return@collect
            socket = Socket(
                socketUrl, SocketOptions(
                    queryParams = mapOf(
                        "_token" to "Bearer ${user.token}"
                    )
                )
            ) {
                onMessageReceived()
                onSeenReceived()
                onRoomCreated()
            }.apply {
                connect()
                emit(SocketEvents.JOIN, "")
            }
        }
    }

    private fun SocketBuilder.onMessageReceived() {
        on<Message>(SocketEvents.SEND_MESSAGE) { data ->
            scope.launch(Dispatchers.IO) {
                chatRepository.updateMessages(messages = listOf(data))
                roomRepository.updateLastMessageRoom(data.roomId, data.content)
            }
        }
    }

    private fun SocketBuilder.onSeenReceived() {
        on<SeenMessageResponse>(SocketEvents.SEEN) { data ->
            scope.launch(Dispatchers.IO) {
                chatRepository.seenMessage(messageId = data.messageId, userId = data.userId)
            }
        }
    }

    private fun SocketBuilder.onRoomCreated() {
        on<Room>(SocketEvents.ROOM) { data ->
            scope.launch(Dispatchers.IO) {
                roomRepository.saveRooms(listOf(data))
            }
        }
    }

    inline fun <reified T> SocketBuilder.on(
        event: String,
        crossinline action: Socket.(message: T) -> Unit
    ) {
        on(event) { data ->
            action(json.decodeFromString<T>(data))
        }
    }

    override

    fun disconnect() {
        socket?.disconnect()
        scope.cancel()
    }

    override fun sendMessage(message: String, roomId: Long) {
        scope.launch(Dispatchers.IO) {
            val encodedMessage =
                json.encodeToString(SendMessageBody(content = message, roomId = roomId))
            socket?.emit(SocketEvents.SEND_MESSAGE, encodedMessage)
        }
    }

    override fun seenMessage(messageId: Long) {
        scope.launch(Dispatchers.IO) {
            val encodedMessage =
                json.encodeToString(SeenMessageBody(messageId = messageId))
            socket?.emit(SocketEvents.SEEN, encodedMessage)
        }
    }

    override fun createRoom(userId: String) {
        scope.launch(Dispatchers.IO) {
            val encodedMessage =
                json.encodeToString(CreateRoomBody(userId = userId))
            socket?.emit(SocketEvents.ROOM, encodedMessage)
        }
    }
}