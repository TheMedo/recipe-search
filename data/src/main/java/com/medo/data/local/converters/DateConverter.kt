package com.medo.data.local.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    @TypeConverter
    fun fromDate(value: Long?): Date? {
        value ?: return null
        return Date(value)
    }

    @TypeConverter
    fun toDate(value: Date?): Long? {
        value ?: return null
        return value.time
    }
}