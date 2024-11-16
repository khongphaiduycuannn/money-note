package com.ndmq.moneynote.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.model.FixedCost
import com.ndmq.moneynote.data.model.Note

@Database(entities = [
    Note::class, Category::class, FixedCost::class
], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {

        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "room_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}