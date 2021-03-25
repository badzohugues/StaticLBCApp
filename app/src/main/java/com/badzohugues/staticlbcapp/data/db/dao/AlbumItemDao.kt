package com.badzohugues.staticlbcapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.badzohugues.staticlbcapp.data.db.DbAlbumItem

private const val GET_ALL = "SELECT * FROM albumItem ORDER BY id ASC"
private const val SELECT_BY_ALBUM_ID = "SELECT * FROM albumItem WHERE albumId LIKE (:id)"

@Dao
interface AlbumItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(albumItem: DbAlbumItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albumItems: List<DbAlbumItem>)

    @Query(GET_ALL)
    suspend fun getAll(): List<DbAlbumItem>

    @Query(SELECT_BY_ALBUM_ID)
    suspend fun getItemsOfAlbum(id: Int): List<DbAlbumItem>
}