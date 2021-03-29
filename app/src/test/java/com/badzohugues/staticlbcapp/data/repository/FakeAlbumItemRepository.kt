package com.badzohugues.staticlbcapp.data.repository

import com.badzohugues.staticlbcapp.data.api.datasource.FakeApi
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.misc.ResultWrapper
import com.badzohugues.staticlbcapp.misc.Status
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class FakeAlbumItemRepository : Repository {

    private val albumItem: MutableList<AlbumItem> = mutableListOf()
    private var shouldReturnError = true

    override suspend fun fetchAllAlbumItemAsync(): Deferred<ResultWrapper<List<AlbumItem>>> {
        return withContext(Dispatchers.IO) {
            async { FakeApi().getAllAlbumItems(shouldReturnError) }
        }
    }

    override suspend fun saveAllAlbumItemsAsync(): Deferred<ResultWrapper<Boolean>> = withContext(Dispatchers.IO) {
        async {
            val result = fetchAllAlbumItemAsync().await()
            if (result.status == Status.SUCCESS) {
                if (!result.data.isNullOrEmpty()) {
                    albumItem.addAll(result.data ?: emptyList())
                }
                ResultWrapper.success(true)
            } else ResultWrapper.error(result.message, false)
        }
    }

    override suspend fun getAlbumsAsync(): Deferred<ResultWrapper<List<AlbumItem>>> = withContext(Dispatchers.IO) {
        async {
            val result = saveAllAlbumItemsAsync().await()

            if (result.status == Status.SUCCESS) ResultWrapper.success(albumItem)
            else ResultWrapper.error(result.message, emptyList())
        }
    }

    override suspend fun getAlbums(): List<AlbumItem> = albumItem

    override suspend fun getItemsOfAlbum(albumId: Int): List<AlbumItem> = albumItem.filter { item -> item.albumId == albumId }


    fun shouldReturnNetworkError(value: Boolean) {
        shouldReturnError = value
    }
}