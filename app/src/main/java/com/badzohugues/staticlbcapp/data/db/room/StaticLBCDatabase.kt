package com.badzohugues.staticlbcapp.data.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.badzohugues.staticlbcapp.data.db.DbAlbumItem
import com.badzohugues.staticlbcapp.data.db.dao.AlbumItemDao

@Database(entities = [DbAlbumItem::class], version = 1, exportSchema = false)
abstract class StaticLBCDatabase : RoomDatabase() {
    abstract fun albumItemDao(): AlbumItemDao

    companion object {
        @Volatile
        private var INSTANCE: StaticLBCDatabase? = null

        fun getDatabase(context: Context): StaticLBCDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StaticLBCDatabase::class.java,
                    "static_lbc_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}