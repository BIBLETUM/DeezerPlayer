package com.example.deezerplayer.data.network.model

import com.google.gson.annotations.SerializedName

data class ChartResponseDto(
    @SerializedName("tracks")
    val tracks: List<TrackDto>,
)