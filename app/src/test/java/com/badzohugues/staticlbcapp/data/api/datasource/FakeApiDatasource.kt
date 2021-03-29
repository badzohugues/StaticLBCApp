package com.badzohugues.staticlbcapp.data.api.datasource

import com.badzohugues.staticlbcapp.data.api.ApiAlbumItem
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.mapper.Mapper
import com.badzohugues.staticlbcapp.misc.ResultWrapper

class FakeApiDatasource(private val apiAlbumItem: List<ApiAlbumItem> = emptyList()) : Mapper<ApiAlbumItem, AlbumItem> {

    fun getAllAlbumItems(returnError: Boolean): ResultWrapper<List<AlbumItem>> {
        return if(returnError) ResultWrapper.error("Error", null)
        else ResultWrapper.success(apiAlbumItem.map { transform(it) })
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