package com.example.data.network.model

import com.google.gson.annotations.SerializedName

data class TracksListDto(
    @SerializedName("data")
    val tracks: List<TrackDto>,
)