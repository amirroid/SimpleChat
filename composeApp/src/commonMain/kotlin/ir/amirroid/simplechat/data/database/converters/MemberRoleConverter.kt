package ir.amirroid.simplechat.data.database.converters

import androidx.room.TypeConverter
import ir.amirroid.simplechat.models.room.MemberRole

class MemberRoleConverter {
    @TypeConverter
    fun fromMemberRole(value: MemberRole): String = value.name

    @TypeConverter
    fun toMemberRole(value: String): MemberRole = MemberRole.valueOf(value)
}
