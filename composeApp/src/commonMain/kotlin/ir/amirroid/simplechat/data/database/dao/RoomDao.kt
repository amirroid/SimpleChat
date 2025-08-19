package ir.amirroid.simplechat.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import ir.amirroid.simplechat.data.database.entities.RoomEntity
import ir.amirroid.simplechat.data.database.entities.RoomMemberEntity
import ir.amirroid.simplechat.data.database.relations.RoomWithMembers
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Transaction
    @Query("SELECT * FROM rooms")
    fun getAllRoomsWithMembersAndUsers(): Flow<List<RoomWithMembers>>


    @Upsert
    suspend fun upsertRooms(rooms: List<RoomEntity>)

    @Upsert
    suspend fun upsertMembers(members: List<RoomMemberEntity>)
}