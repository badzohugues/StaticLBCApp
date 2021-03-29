package com.badzohugues.staticlbcapp.data.repository

import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.misc.ResultWrapper
import kotlinx.coroutines.Deferred

interface Repository {
    suspend fun fetchAllAlbumItemAsync(): Deferred<ResultWrapper<List<AlbumItem>>>

    suspend fun saveAllAlbumItemsAsync(): Deferred<ResultWrapper<Boolean>>

    suspend fun getAlbumsAsync(): Deferred<ResultWrapper<List<AlbumItem>>>

    suspend fun getAlbums(): List<AlbumItem>

    suspend fun getItemsOfAlbum(albumId: Int): List<AlbumItem>
}