package com.badzohugues.staticlbcapp.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.badzohugues.staticlbcapp.data.db.DbAlbumItem
import com.badzohugues.staticlbcapp.data.db.dao.AlbumItemDao

@Database(entities = [DbAlbumItem::class], version = 1, exportSchema = false)
abstract class StaticLBCDatabase : RoomDatabase() {
    abstract fun albumItemDao(): AlbumItemDao
}