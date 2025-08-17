package ir.amirroid.simplechat.database.message.mapper

import ir.amirroid.simplechat.data.models.message.Message
import ir.amirroid.simplechat.database.message.MessageTable
import ir.amirroid.simplechat.database.room.RoomMemberTable
import ir.amirroid.simplechat.database.room.mapper.toRoomMember
import org.jetbrains.exposed.v1.core.Alias
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toMessage(roomMemberAlias: Alias<RoomMemberTable>, myUserId: String): Message {
    return Message(
        id = this[MessageTable.id].value,
        content = this[MessageTable.content],
        createdAt = this[MessageTable.createdAt],
        updatedAt = this[MessageTable.updatedAt],
        sender = this.toRoomMember(roomMemberAlias, myUserId),
        roomId = this[MessageTable.roomId].value
    )
}