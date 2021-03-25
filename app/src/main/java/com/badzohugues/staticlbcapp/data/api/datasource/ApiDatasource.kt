package com.badzohugues.staticlbcapp.data.api.datasource

import com.badzohugues.staticlbcapp.data.api.ApiAlbumItem
import com.badzohugues.staticlbcapp.data.api.factory.ApiFactory
import com.badzohugues.staticlbcapp.data.api.service.AlbumItemApiService
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.mapper.Mapper

class ApiDatasource : Mapper<ApiAlbumItem, AlbumItem> {
    private val albumItemApiService: AlbumItemApiService by lazy {
        ApiFactory.retrofitBuilder
            .build()
            .create(AlbumItemApiService::class.java)
    }

    suspend fun getAlbumItems(): List<AlbumItem> {
        return albumItemApiService.fetchAlbumItems().map { transform(it) }
    }

    suspend fun getAlbums(): List<AlbumItem> {
        var currentAlbumId = 0
        val sortedList: MutableList<AlbumItem> = ArrayList()

        albumItemApiService.fetchAlbumItems().sortedBy { it.albumId }.map { apiItem ->
            if (apiItem.albumId == 0 || apiItem.albumId != currentAlbumId) {
                sortedList.add(transform(apiItem))
                currentAlbumId = apiItem.albumId ?: 0
            }
        }
        return sortedList.distinctBy { it.albumId }
    }

    override fun transform(item: ApiAlbumItem): AlbumItem {
        return AlbumItem(
            albumId = item.albumId ?: 0,
            id = item.id ?: 0,
            title = item.title ?: String(),
            url = item.url ?: String(),
            thumbnailUrl = item.thumbnailUrl ?: String()
        )
    }
}