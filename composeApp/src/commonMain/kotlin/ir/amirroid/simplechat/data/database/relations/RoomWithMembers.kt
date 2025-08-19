package ir.amirroid.simplechat.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ir.amirroid.simplechat.data.database.entities.RoomEntity
import ir.amirroid.simplechat.data.database.entities.RoomMemberEntity
import ir.amirroid.simplechat.data.database.entities.UserEntity

data class RoomWithMembers(
    @Embedded val room: RoomEntity,

    @Relation(
        RoomMemberEntity::class,
        "id",
        "roomId"
    )
    val members: List<MemberWithUser>
)

data class MemberWithUser(
    @Embedded val member: RoomMemberEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val user: UserEntity
)