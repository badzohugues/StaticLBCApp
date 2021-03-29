package com.badzohugues.staticlbcapp.data.api.datasource

import com.badzohugues.staticlbcapp.data.api.ApiAlbumItem
import com.badzohugues.staticlbcapp.data.api.service.AlbumItemApiService
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.mapper.Mapper
import com.badzohugues.staticlbcapp.misc.ErrorMessage
import com.badzohugues.staticlbcapp.misc.ResultWrapper
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

private const val TIMEOUT = 7000L

class ApiDatasource @Inject constructor(private val albumItemApiService: AlbumItemApiService) :
    Mapper<ApiAlbumItem, AlbumItem> {

    suspend fun getAllAlbumItems(): ResultWrapper<List<AlbumItem>> {
        return try {
            withTimeout(TIMEOUT) {
                val result = albumItemApiService.fetchAllAlbumItems()
                if (result.isSuccessful) {
                    ResultWrapper.success(result.body()?.map { transform(it) } ?: emptyList())
                } else {
                    ResultWrapper.error(ErrorMessage.SERVER_ERROR.message, emptyList())
                }
            }
        } catch (cause: Throwable) {
            ResultWrapper.error(cause.message, emptyList())
        }
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