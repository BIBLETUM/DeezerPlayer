package com.example.data.network.model

import com.google.gson.annotations.SerializedName

data class SearchResponseDto(
    @SerializedName("data")
    val dataList: List<SearchItemDto>,
)
