package com.example.deezerplayer.data.network

import com.example.deezerplayer.data.network.model.ChartResponseDto
import com.example.deezerplayer.data.network.model.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("chart")
    suspend fun getChart(): ChartResponseDto

    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String,
    ): SearchResponseDto

}