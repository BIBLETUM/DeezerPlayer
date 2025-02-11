package com.example.data.network.model

import com.google.gson.annotations.SerializedName

data class ArtistDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("picture")
    val picture: String,
)
