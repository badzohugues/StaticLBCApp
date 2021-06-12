package com.badzohugues.staticlbcapp.data.repository

import com.badzohugues.staticlbcapp.data.api.ApiAlbumItem
import com.badzohugues.staticlbcapp.data.api.datasource.FakeApiDatasource
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.misc.ResultWrapper
import com.badzohugues.staticlbcapp.misc.Status
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class FakeAlbumItemRepository(private var forceError: Boolean = false) : Repository {

    private val albumItem: MutableList<AlbumItem> = mutableListOf()
    private val apiAlbumItemA = ApiAlbumItem(
        424242,
        42,
        "AlbumItemTest",
        "https://via.placeholder.com/600/e403d1",
        "https://via.placeholder.com/150/e403d1"
    )

    private val apiAlbumItemB = ApiAlbumItem(
        242424,
        42,
        "AlbumItemTest",
        "https://via.placeholder.com/600/e403d1",
        "https://via.placeholder.com/150/e403d1"
    )

    private val apiAlbumItemC = ApiAlbumItem(
        212142,
        24,
        "AlbumItemTest",
        "https://via.placeholder.com/600/e403d1",
        "https://via.placeholder.com/150/e403d1"
    )

    val apiDatasource =
        FakeApiDatasource(mutableListOf(apiAlbumItemA, apiAlbumItemB, apiAlbumItemC))

    override suspend fun fetchAllAlbumItemAsync(): Deferred<ResultWrapper<List<AlbumItem>>> {
        return withContext(Dispatchers.IO) {
            async { apiDatasource.getAllAlbumItems(forceError) }
        }
    }

    override suspend fun saveAllAlbumItemsAsync(): Deferred<ResultWrapper<Boolean>> =
        withContext(Dispatchers.IO) {
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

    override suspend fun getAlbumsAsync(): Deferred<ResultWrapper<List<AlbumItem>>> =
        withContext(Dispatchers.IO) {
            async {
                val result = saveAllAlbumItemsAsync().await()

                if (result.status == Status.SUCCESS) ResultWrapper.success(albumItem)
                else ResultWrapper.error(result.message, emptyList())
            }
        }

    override suspend fun getAlbums(): List<AlbumItem> = albumItem

    override suspend fun getItemsOfAlbum(albumId: Int): List<AlbumItem> {
        return albumItem.filter { item -> item.albumId == albumId }
    }

    fun toForceNetworkError(value: Boolean) {
        forceError = value
    }
}
