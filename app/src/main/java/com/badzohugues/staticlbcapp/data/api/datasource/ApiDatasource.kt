package com.badzohugues.staticlbcapp.data.api.datasource

import com.badzohugues.staticlbcapp.data.api.ApiAlbumItem
import com.badzohugues.staticlbcapp.data.api.service.AlbumItemApiService
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.mapper.Mapper
import com.badzohugues.staticlbcapp.misc.ErrorMessage
import com.badzohugues.staticlbcapp.misc.ResultWrapper
import java.lang.Exception
import javax.inject.Inject

class ApiDatasource @Inject constructor(private val albumItemApiService: AlbumItemApiService) : Mapper<ApiAlbumItem, AlbumItem> {

    suspend fun getAllAlbumItems(): List<AlbumItem> {
        return albumItemApiService.fetchAllAlbumItems().map { transform(it) }
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