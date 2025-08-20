package ir.amirroid.simplechat.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import ir.amirroid.simplechat.data.database.entities.MessageEntity
import ir.amirroid.simplechat.data.database.entities.MessageStatusEntity
import ir.amirroid.simplechat.data.database.relations.MessageWithSenderAndStatuses
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Transaction
    @Query(
        """
    SELECT * 
    FROM messages
    WHERE roomId = :roomId
    ORDER BY createdAt DESC
    """
    )
    fun getMessagesWithSenderAndStatuses(
        roomId: Long
    ): Flow<List<MessageWithSenderAndStatuses>>



    @Query("DELETE FROM messages")
    suspend fun clearAll()


    @Upsert
    suspend fun upsertMessages(messages: List<MessageEntity>)


    @Upsert
    suspend fun upsertMessageStatuses(statuses: List<MessageStatusEntity>)

    @Upsert
    suspend fun upsertMessageStatus(status: MessageStatusEntity)
}