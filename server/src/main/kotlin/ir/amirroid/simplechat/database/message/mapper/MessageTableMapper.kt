package ir.amirroid.simplechat.database.message.mapper

import ir.amirroid.simplechat.models.message.Message
import ir.amirroid.simplechat.models.message.MessageStatus
import ir.amirroid.simplechat.models.room.RoomMember
import ir.amirroid.simplechat.database.message.MessageTable
import ir.amirroid.simplechat.database.room_member.RoomMemberTable
import ir.amirroid.simplechat.database.room_member.mapper.toRoomMember
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

fun ResultRow.toMessage(
    sender: RoomMember,
    statuses: List<MessageStatus>
): Message {
    return Message(
        id = this[MessageTable.id].value,
        content = this[MessageTable.content],
        createdAt = this[MessageTable.createdAt],
        updatedAt = this[MessageTable.updatedAt],
        sender = sender,
        roomId = this[MessageTable.roomId].value,
        statuses = statuses
    )
}