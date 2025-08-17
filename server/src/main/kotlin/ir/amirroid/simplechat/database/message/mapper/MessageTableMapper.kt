package ir.amirroid.simplechat.database.message.mapper

import ir.amirroid.simplechat.data.models.message.Message
import ir.amirroid.simplechat.data.models.message.MessageStatus
import ir.amirroid.simplechat.database.message.MessageTable
import ir.amirroid.simplechat.database.room.RoomMemberTable
import ir.amirroid.simplechat.database.room.mapper.toRoomMember
import ir.amirroid.simplechat.database.user.UserTable
import org.jetbrains.exposed.v1.core.Alias
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toMessage(
    roomMemberAlias: Alias<RoomMemberTable>,
    userSenderAlias: Alias<UserTable>,
    myUserId: String,
    statuses: List<MessageStatus>
): Message {
    return Message(
        id = this[MessageTable.id].value,
        content = this[MessageTable.content],
        createdAt = this[MessageTable.createdAt],
        updatedAt = this[MessageTable.updatedAt],
        sender = this.toRoomMember(roomMemberAlias, userSenderAlias, myUserId),
        roomId = this[MessageTable.roomId].value,
        statuses = statuses
    )
}