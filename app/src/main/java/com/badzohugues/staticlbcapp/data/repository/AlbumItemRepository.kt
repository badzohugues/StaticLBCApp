package com.badzohugues.staticlbcapp.data.repository

import com.badzohugues.staticlbcapp.data.api.datasource.ApiDatasource
import com.badzohugues.staticlbcapp.data.db.datasource.DbDatasource
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.misc.ResultWrapper
import com.badzohugues.staticlbcapp.misc.Status
import kotlinx.coroutines.*
import javax.inject.Inject

class AlbumItemRepository @Inject constructor(
    private val dbDatasource: DbDatasource,
    private val apiDatasource: ApiDatasource
) : Repository {

    override suspend fun fetchAllAlbumItemAsync(): Deferred<ResultWrapper<List<AlbumItem>>> =
        withContext(Dispatchers.IO) {
            async { apiDatasource.getAllAlbumItems() }
        }

    override suspend fun getAlbumsAsync(): Deferred<ResultWrapper<List<AlbumItem>>> =
        withContext(Dispatchers.IO) {
            async {
                val result = saveAllAlbumItemsAsync().await()

                if (result.status == Status.SUCCESS) ResultWrapper.success(dbDatasource.getAllAlbums())
                else ResultWrapper.error(result.message, emptyList())
            }
        }

    override suspend fun saveAllAlbumItemsAsync(): Deferred<ResultWrapper<Boolean>> =
        withContext(Dispatchers.IO) {
            async {
                val result = fetchAllAlbumItemAsync().await()

                if (result.status == Status.SUCCESS) {
                    if (!result.data.isNullOrEmpty()) dbDatasource.insertAll(result.data)
                    ResultWrapper.success(true)
                } else ResultWrapper.error(result.message, false)
            }
        }

    override suspend fun getAlbums(): List<AlbumItem> = dbDatasource.getAllAlbums()

    override suspend fun getItemsOfAlbum(albumId: Int): List<AlbumItem> = dbDatasource.getItemsOfAlbum(albumId)
}