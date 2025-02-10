package com.example.deezerplayer.domain.repository

import com.example.deezerplayer.domain.Track
import kotlinx.coroutines.flow.Flow

interface ChartRepository {

    fun getTracksFlow(): Flow<List<Track>>

    suspend fun searchTracks(query: String)

}