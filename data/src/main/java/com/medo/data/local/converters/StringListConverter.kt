package com.medo.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        value ?: return null
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        list ?: return null
        return Gson().toJson(list)
    }
}