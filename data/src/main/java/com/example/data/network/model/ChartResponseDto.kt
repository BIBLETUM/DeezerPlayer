package com.example.data.network.model

import com.google.gson.annotations.SerializedName

data class ChartResponseDto(
    @SerializedName("tracks")
    val trackList: TracksListDto,
)