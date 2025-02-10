package com.example.deezerplayer.data.network.model

import com.google.gson.annotations.SerializedName

data class SearchItemDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("preview")
    val preview: String,
    @SerializedName("artist")
    val artist: ArtistDto,
    @SerializedName("album")
    val album: AlbumDto,
    @SerializedName("type")
    val type: String
)
