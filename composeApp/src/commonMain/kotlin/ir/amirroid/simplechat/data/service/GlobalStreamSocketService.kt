package ir.amirroid.simplechat.data.service

interface GlobalStreamSocketService {
    suspend fun connect()
    fun disconnect()
    fun sendMessage(message: String, roomId: Long)
    fun seenMessage(messageId: Long)
    fun createRoom(userId: String)
}