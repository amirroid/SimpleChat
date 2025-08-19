package ir.amirroid.simplechat.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import ir.amirroid.simplechat.data.database.converters.LocalDateTimeConverter
import ir.amirroid.simplechat.data.database.converters.MemberRoleConverter
import ir.amirroid.simplechat.data.database.dao.RoomDao
import ir.amirroid.simplechat.data.database.dao.UserDao
import ir.amirroid.simplechat.data.database.entities.RoomEntity
import ir.amirroid.simplechat.data.database.entities.RoomMemberEntity
import ir.amirroid.simplechat.data.database.entities.UserEntity

@ConstructedBy(AppDatabaseConstructor::class)
@Database(
    entities = [RoomEntity::class, RoomMemberEntity::class, UserEntity::class],
    version = 1
)
@TypeConverters(
    MemberRoleConverter::class,
    LocalDateTimeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun userDao(): UserDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}