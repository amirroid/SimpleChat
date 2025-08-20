package ir.amirroid.simplechat.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ir.amirroid.simplechat.data.database.entities.MessageEntity
import ir.amirroid.simplechat.data.database.entities.MessageStatusEntity
import ir.amirroid.simplechat.data.database.entities.RoomMemberEntity
import ir.amirroid.simplechat.data.database.entities.UserEntity

data class MessageWithSenderAndStatuses(
    @Embedded val message: MessageEntity,
    @Relation(
        entity = RoomMemberEntity::class,
        parentColumn = "senderUserId",
        entityColumn = "userId"
    )
    val sender: MemberWithUser,
    @Relation(
        MessageStatusEntity::class,
        parentColumn = "id",
        entityColumn = "messageId"
    ) val statuses: List<StatusWithUser>
)

data class StatusWithUser(
    @Embedded val status: MessageStatusEntity,
    @Relation(
        UserEntity::class,
        parentColumn = "userId",
        entityColumn = "userId"
    ) val user: UserEntity
)