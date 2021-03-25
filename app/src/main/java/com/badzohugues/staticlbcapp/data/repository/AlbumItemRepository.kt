package com.badzohugues.staticlbcapp.data.repository

import com.badzohugues.staticlbcapp.data.api.datasource.ApiDatasource
import com.badzohugues.staticlbcapp.data.db.dao.AlbumItemDao
import com.badzohugues.staticlbcapp.data.db.datasource.DbDatasource
import com.badzohugues.staticlbcapp.data.domain.AlbumItem

class AlbumItemRepository(private val albumItemDao: AlbumItemDao) {
    private val dbDatasource by lazy { DbDatasource(albumItemDao) }
    private val apiDatasource by lazy { ApiDatasource() }

    suspend fun fetchAllAlbumItem(): List<AlbumItem> {
        return apiDatasource.getAlbumItems()
    }

    suspend fun getAlbums(isConnected: Boolean): List<AlbumItem> {
        return if(isConnected) apiDatasource.getAlbums()
        else dbDatasource.getAllAlbums()
    }

    suspend fun saveAllAlbumItems(albumItems: List<AlbumItem>) {
        if (dbDatasource.getAllAlbums().isEmpty()) dbDatasource.insertAll(albumItems)
    }

    suspend fun getItemsOfAlbum(albumId: Int): List<AlbumItem> {
        return dbDatasource.getAlbumItems(albumId)
    }
}