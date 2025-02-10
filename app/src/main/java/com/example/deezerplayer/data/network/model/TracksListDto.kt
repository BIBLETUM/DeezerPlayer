package com.example.deezerplayer.data.network.model

import com.google.gson.annotations.SerializedName

data class TracksListDto(
    @SerializedName("data")
    val tracks: List<TrackDto>,
)