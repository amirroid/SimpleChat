package ir.amirroid.simplechat.data.repository.chat

import androidx.room.useWriterConnection
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import ir.amirroid.simplechat.data.call.RetryCallExecutor
import ir.amirroid.simplechat.data.call.SafeApiCall
import ir.amirroid.simplechat.data.database.AppDatabase
import ir.amirroid.simplechat.data.database.dao.MessageDao
import ir.amirroid.simplechat.data.database.dao.RoomDao
import ir.amirroid.simplechat.data.database.dao.UserDao
import ir.amirroid.simplechat.data.database.entities.MessageStatusEntity
import ir.amirroid.simplechat.data.mappers.toEntity
import ir.amirroid.simplechat.data.mappers.toMessageStatus
import ir.amirroid.simplechat.data.mappers.toRoomMember
import ir.amirroid.simplechat.data.mappers.toUser
import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.data.response.onSuccess
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.message.Message
import ir.amirroid.simplechat.models.message.MessageDeliveryStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ChatRepositoryImpl(
    private val httpClient: HttpClient,
    private val messageDao: MessageDao,
    private val roomDao: RoomDao,
    private val userDao: UserDao,
    private val appDatabase: AppDatabase
) : ChatRepository {
    suspend fun getAllChats(roomId: Long): Response<DefaultRequiredResponse<List<Message>>, NetworkErrors> {
        return SafeApiCall.launch { httpClient.get("rooms/$roomId/messages") }
    }

    override suspend fun fetchAllChatsAndSave(roomId: Long): Response<DefaultRequiredResponse<List<Message>>, NetworkErrors> {
        return RetryCallExecutor.call(times = Int.MAX_VALUE) { getAllChats(roomId) }
            .onSuccess { response ->
                val messages = response.data
                updateMessages(messages, withClearData = true)
            }
    }

    override suspend fun updateMessages(messages: List<Message>, withClearData: Boolean) {
//        appDatabase.useWriterConnection {
        if (withClearData) messageDao.clearAll()
        messageDao.upsertMessages(
            messages.map { it.toEntity() }
        )
        userDao.upsertUsers(
            messages.map { it.sender.user.toEntity() }
        )
        roomDao.upsertMembers(
            messages.map { it.sender.toEntity(it.roomId) }
        )
        userDao.upsertUsers(
            messages.flatMap { it.statuses.map { status -> status.user.toEntity() } }
        )
        messageDao.upsertMessageStatuses(
            messages.flatMap { it.statuses.map { status -> status.toEntity() } }
        )
//        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun seenMessage(messageId: Long, userId: String) {
        messageDao.upsertMessageStatus(
            MessageStatusEntity(
                messageId,
                userId,
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                status = MessageDeliveryStatus.SEEN
            )
        )
    }

    override fun getAllChatsFromLocal(roomId: Long): Flow<List<Message>> {
        return messageDao.getMessagesWithSenderAndStatuses(roomId)
            .map { messages ->
                messages.map { (message, sender, statuses) ->
                    Message(
                        id = message.id,
                        content = message.content,
                        updatedAt = message.updatedAt,
                        createdAt = message.createdAt,
                        roomId = message.roomId,
                        statuses = statuses.map { it.toMessageStatus() },
                        sender = sender.member.toRoomMember(sender.user.toUser())
                    )
                }
            }
    }
}