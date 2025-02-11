package com.example.data.network.model

import com.google.gson.annotations.SerializedName

data class AlbumDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("cover")
    val cover: String,
)
