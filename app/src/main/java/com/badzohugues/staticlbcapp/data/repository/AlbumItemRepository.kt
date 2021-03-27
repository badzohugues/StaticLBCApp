package com.badzohugues.staticlbcapp.data.repository

import com.badzohugues.staticlbcapp.data.api.datasource.ApiDatasource
import com.badzohugues.staticlbcapp.data.db.datasource.DbDatasource
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import javax.inject.Inject

class AlbumItemRepository @Inject constructor(private val dbDatasource: DbDatasource,
                                              private val apiDatasource: ApiDatasource) : Repository {

    override suspend fun fetchAllAlbumItem(): List<AlbumItem> {
        return apiDatasource.getAllAlbumItems()
    }

    override fun getAlbums(): List<AlbumItem> = dbDatasource.getAllAlbums()

    override suspend fun saveAllAlbumItems(albumItems: List<AlbumItem>) {
        dbDatasource.insertAll(albumItems)
    }

    override suspend fun getItemsOfAlbum(albumId: Int): List<AlbumItem> = dbDatasource.getItemsOfAlbum(albumId)
}