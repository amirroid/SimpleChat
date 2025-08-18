package ir.amirroid.simplechat.database.room_member

import ir.amirroid.simplechat.database.room.RoomTable
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.models.room.MemberRole
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object RoomMemberTable : Table("room_members") {
    val roomId = reference("room_id", RoomTable.id, ReferenceOption.CASCADE)
    val userId = reference("user_id", UserTable.userId, ReferenceOption.CASCADE)
    val role = enumeration("role", MemberRole::class).default(MemberRole.MEMBER)
    val joinedAt = datetime("joined_at").defaultExpression(CurrentDateTime)

    init {
        index(true, roomId, userId)
    }

    override val primaryKey = PrimaryKey(roomId, userId)
}