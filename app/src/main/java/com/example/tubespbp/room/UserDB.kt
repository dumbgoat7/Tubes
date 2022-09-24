package com.example.tubespbp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, News::class],
    version = 1,
)

abstract class UserDB: RoomDatabase() {
    abstract fun UserDao() : UserDao
    abstract fun NewsDao() : NewsDao

    companion object {
        @Volatile
        private var INSTANCE : UserDB? = null
        private val LOCK = Any()

        fun getDatabase(context: Context): UserDB? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    UserDB::class.java, "user12345.db"
                )
                    .build()
            }
            return INSTANCE
        }
        operator fun invoke(context: Context) = INSTANCE ?:
        synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDB::class.java,
            "user12345.db"
        ).allowMainThreadQueries().build()
    }
}