package com.badzohugues.staticlbcapp.data.db.datasource

import com.badzohugues.staticlbcapp.data.db.DbAlbumItem
import com.badzohugues.staticlbcapp.data.db.dao.AlbumItemDao
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.mapper.TwoWayMapper
import javax.inject.Inject

class DbDatasource @Inject constructor(
  private val albumItemDao: AlbumItemDao
) : TwoWayMapper<AlbumItem, DbAlbumItem> {

    override fun transform(item: AlbumItem): DbAlbumItem {
        return DbAlbumItem(
            albumId = item.albumId,
            id = item.id,
            title = item.title,
            url = item.url,
            thumbnailUrl = item.thumbnailUrl
        )
    }

    override fun revert(item: DbAlbumItem): AlbumItem {
        return AlbumItem(
            albumId = item.albumId,
            id = item.id,
            title = item.title,
            url = item.url,
            thumbnailUrl = item.thumbnailUrl
        )
    }

    suspend fun insert(item: AlbumItem) {
        albumItemDao.insert(transform(item))
    }

    suspend fun insertAll(items: List<AlbumItem>) {
        albumItemDao.insertAll(items.map { transform(it) })
    }

    private fun getAllAlbumItem(): List<AlbumItem> {
        return albumItemDao.getAll().map { dbItem -> revert(dbItem) }
    }

    fun getAllAlbums(): List<AlbumItem> {
        return getAllAlbumItem().distinctBy { it.albumId }
    }

    suspend fun getItemsOfAlbum(albumId: Int): List<AlbumItem> {
        return albumItemDao.getItemsOfAlbum(albumId).map { dbItem -> revert(dbItem) }
    }
}