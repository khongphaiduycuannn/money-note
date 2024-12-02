package com.ndmq.moneynote.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ndmq.moneynote.data.model.Category
import java.util.Date

class RoomConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun categoryToJson(category: Category): String {
        return Gson().toJson(category)
    }

    @TypeConverter
    fun jsonToCategory(json: String): Category {
        return Gson().fromJson(json, Category::class.java)
    }
}