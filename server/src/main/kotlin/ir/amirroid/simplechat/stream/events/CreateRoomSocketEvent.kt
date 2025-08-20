package ir.amirroid.simplechat.stream.events

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import ir.amirroid.simplechat.database.room.service.RoomsService
import ir.amirroid.simplechat.database.room_member.service.RoomMemberService
import ir.amirroid.simplechat.database.room_member.service.UserIdWithRole
import ir.amirroid.simplechat.database.user.service.UserService
import ir.amirroid.simplechat.models.body.CreateRoomBody
import ir.amirroid.simplechat.models.room.MemberRole
import ir.amirroid.simplechat.socket.events.SocketEventListener
import ir.amirroid.simplechat.stream.StreamSocketManagerImpl
import ir.amirroid.simplechat.utils.SocketEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class CreateRoomSocketEvent(
    private val streamSocketManager: StreamSocketManagerImpl,
    private val roomMemberService: RoomMemberService,
    private val roomService: RoomsService,
    private val userService: UserService,
    private val json: Json
) : SocketEventListener<CreateRoomBody>(SocketEvents.ROOM) {
    override fun handleEventListener(
        client: SocketIOClient,
        data: CreateRoomBody,
        ackSender: AckRequest
    ) {
        val user = streamSocketManager.getUserFromClient(client) ?: return

        socketManager.coroutineScop.launch(Dispatchers.IO) {
            if (userService.existsByUserId(data.userId).not()) return@launch
            val roomId = roomService.createRoom(user.userId)
            val members = listOf(
                UserIdWithRole(userId = user.userId, role = MemberRole.OWNER),
                UserIdWithRole(userId = data.userId, role = MemberRole.MEMBER)
            )
            roomMemberService.addMembers(roomId, members)
            members.forEach { (userId, _) ->
                val room = roomService.getUserRoomById(userId, roomId)
                streamSocketManager.clients[userId]?.sendEvent(
                    SocketEvents.ROOM,
                    json.encodeToString(room)
                )
            }
        }
    }
}