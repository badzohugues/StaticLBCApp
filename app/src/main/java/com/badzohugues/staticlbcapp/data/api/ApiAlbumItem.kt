package com.badzohugues.staticlbcapp.data.api

import com.google.gson.annotations.SerializedName

data class ApiAlbumItem(
    @SerializedName("albumId") val albumId: Int?,
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?
)
