package ir.amirroid.simplechat.database.message.services

import ir.amirroid.simplechat.models.message.Message

interface MessagesService {
    suspend fun getAllMessages(roomId: Long, myUserId: String): List<Message>
    suspend fun addMessage(content: String, roomId: Long, userId: String): Message
}