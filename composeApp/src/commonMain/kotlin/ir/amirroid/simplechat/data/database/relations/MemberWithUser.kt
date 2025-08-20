package ir.amirroid.simplechat.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ir.amirroid.simplechat.data.database.entities.RoomMemberEntity
import ir.amirroid.simplechat.data.database.entities.UserEntity


data class MemberWithUser(
    @Embedded val member: RoomMemberEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val user: UserEntity
)