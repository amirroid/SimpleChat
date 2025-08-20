package ir.amirroid.simplechat.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ir.amirroid.simplechat.data.database.entities.RoomEntity
import ir.amirroid.simplechat.data.database.entities.RoomMemberEntity

data class RoomWithMembers(
    @Embedded val room: RoomEntity,

    @Relation(
        RoomMemberEntity::class,
        "id",
        "roomId"
    )
    val members: List<MemberWithUser>
)