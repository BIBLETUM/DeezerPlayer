package com.example.domain.repository

import com.example.domain.Track
import kotlinx.coroutines.flow.Flow

interface ChartRepository {

    fun getTracksFlow(): Flow<List<Track>>

    suspend fun searchTracks(query: String)

}