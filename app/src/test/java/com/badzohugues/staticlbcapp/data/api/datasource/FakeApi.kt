package com.badzohugues.staticlbcapp.data.api.datasource

import com.badzohugues.staticlbcapp.data.api.ApiAlbumItem
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.data.mapper.Mapper
import com.badzohugues.staticlbcapp.misc.ResultWrapper
import org.junit.Before

class FakeApi : Mapper<ApiAlbumItem, AlbumItem> {
    private lateinit var apiAlbumItemA: ApiAlbumItem
    private lateinit var apiAlbumItemB: ApiAlbumItem
    private lateinit var apiAlbumItemC: ApiAlbumItem
    private lateinit var result: List<AlbumItem>

    @Before
    fun setup() {
        apiAlbumItemA = ApiAlbumItem(
            424242,
            42,
            "AlbumItemTest",
            "https://via.placeholder.com/600/e403d1",
            "https://via.placeholder.com/150/e403d1"
        )

        apiAlbumItemB = ApiAlbumItem(
            242424,
            42,
            "AlbumItemTest",
            "https://via.placeholder.com/600/e403d1",
            "https://via.placeholder.com/150/e403d1"
        )

        apiAlbumItemC = ApiAlbumItem(
            212142,
            24,
            "AlbumItemTest",
            "https://via.placeholder.com/600/e403d1",
            "https://via.placeholder.com/150/e403d1"
        )

        result = listOf(transform(apiAlbumItemA),
            transform(apiAlbumItemB),
            transform(apiAlbumItemC)
        )
    }

    fun getAllAlbumItems(returnError: Boolean): ResultWrapper<List<AlbumItem>> {
        return if(returnError) ResultWrapper.error("Error", null)
        else ResultWrapper.success(result)
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