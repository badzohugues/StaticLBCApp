package com.badzohugues.staticlbcapp.data.repository

import androidx.lifecycle.LiveData
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.misc.ResultWrapper
import java.lang.Exception

class FakeAlbumItemRepository : Repository {
    override suspend fun fetchAllAlbumItem(): ResultWrapper<List<AlbumItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumsFromApi(): ResultWrapper<List<AlbumItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbums(): LiveData<List<AlbumItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAllAlbumItems(albumItems: List<AlbumItem>) {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsOfAlbum(albumId: Int): LiveData<List<AlbumItem>> {
        TODO("Not yet implemented")
    }

    /*private val albumItemList = mutableListOf<AlbumItem>()
    private val fetchedApiAlbums: List<AlbumItem> = emptyList()
    private val fetchedDbAlbums: List<AlbumItem> = emptyList()
    private val fetchedAlbumItems: List<AlbumItem> = emptyList()
    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(isNetworkError: Boolean) {
        shouldReturnNetworkError = isNetworkError
    }

    override suspend fun fetchAllAlbumItem(): ResultWrapper<List<AlbumItem>> {
        return if(shouldReturnNetworkError) throw Exception("Network Error") else fetchedAlbumItems
    }

    override suspend fun getAlbumsFromApi(): ResultWrapper<List<AlbumItem>> = fetchedApiAlbums

    override suspend fun getAlbums(): List<AlbumItem> = fetchedDbAlbums

    override suspend fun saveAllAlbumItems(albumItems: List<AlbumItem>) {
        albumItemList.addAll(albumItems)
    }

    override suspend fun getItemsOfAlbum(albumId: Int): List<AlbumItem> {
        return albumItemList.filter { it.albumId == albumId }
    }*/
}