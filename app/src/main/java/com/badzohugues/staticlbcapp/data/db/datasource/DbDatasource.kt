package com.badzohugues.staticlbcapp.data.db.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.badzohugues.staticlbcapp.data.db.DbAlbumItem
import com.badzohugues.staticlbcapp.data.db.dao.AlbumItemDao
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.mapper.TwoWayMapper

class DbDatasource(private val albumItemDao: AlbumItemDao) : TwoWayMapper<AlbumItem, DbAlbumItem> {

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

    suspend fun getAllAlbumItem(): List<AlbumItem> {
        return albumItemDao.getAll().map { dbItem ->
            revert(dbItem)
        }
    }

    suspend fun getAllAlbums(): List<AlbumItem> {
        return getAllAlbumItem().distinctBy { it.albumId }
    }

    suspend fun getAlbumItems(albumId: Int): List<AlbumItem> {
        return albumItemDao.getAlbums(albumId).map {
                dbAlbumItem -> revert(dbAlbumItem)
        }
    }
}