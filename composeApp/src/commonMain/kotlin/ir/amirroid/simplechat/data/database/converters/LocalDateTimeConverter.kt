package ir.amirroid.simplechat.data.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime


class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }
}
