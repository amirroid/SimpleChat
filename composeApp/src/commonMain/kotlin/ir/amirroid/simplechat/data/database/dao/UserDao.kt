package ir.amirroid.simplechat.data.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import ir.amirroid.simplechat.data.database.entities.UserEntity

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUsers(users: List<UserEntity>)
}