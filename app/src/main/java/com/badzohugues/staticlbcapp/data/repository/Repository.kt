package com.badzohugues.staticlbcapp.data.repository

import com.badzohugues.staticlbcapp.data.domain.AlbumItem

interface Repository {
    suspend fun fetchAllAlbumItem(): List<AlbumItem>

    suspend fun saveAllAlbumItems(albumItems: List<AlbumItem>)

    fun getAlbums(): List<AlbumItem>

    suspend fun getItemsOfAlbum(albumId: Int): List<AlbumItem>
}