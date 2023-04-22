package it.polito.mad.playgroundsreservations.database

import androidx.room.TypeConverter
import java.time.Duration
import java.util.*

class TypeConverters {
    @TypeConverter
    fun dateFromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun durationFromLong(value: Long?): Duration? {
        return value?.let { Duration.ofMillis(it) }
    }

    @TypeConverter
    fun durationToLong(duration: Duration?): Long? {
        return duration?.toMillis()
    }
}