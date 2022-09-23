package com.example.tubespbp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [News::class],
    version = 1,
)

abstract class NewsDB: RoomDatabase() {
    abstract fun NewsDao() : NewsDao

    companion object {
        @Volatile
        private var instance : UserDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDB::class.java,
            "news12345.db"
        ).allowMainThreadQueries().build()
    }
}