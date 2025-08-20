package ir.amirroid.simplechat.data.repository.chat

import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.message.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun fetchAllChatsAndSave(roomId: Long): Response<DefaultRequiredResponse<List<Message>>, NetworkErrors>
    suspend fun updateMessages(messages: List<Message>, withClearData: Boolean = false)
    suspend fun seenMessage(messageId: Long, userId: String)
    fun getAllChatsFromLocal(roomId: Long): Flow<List<Message>>
}