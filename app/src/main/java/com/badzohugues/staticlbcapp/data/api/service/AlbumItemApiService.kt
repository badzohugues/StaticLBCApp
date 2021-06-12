package com.badzohugues.staticlbcapp.data.api.service

import com.badzohugues.staticlbcapp.data.api.ApiAlbumItem
import retrofit2.Response
import retrofit2.http.GET

interface AlbumItemApiService {
    @GET("img/shared/technical-test.json")
    suspend fun fetchAllAlbumItems(): Response<List<ApiAlbumItem>>
}
