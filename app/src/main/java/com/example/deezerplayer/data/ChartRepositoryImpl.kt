package com.example.deezerplayer.data

import com.example.deezerplayer.data.network.ApiService
import com.example.deezerplayer.data.network.mapper.TrackMapper
import com.example.deezerplayer.domain.Track
import com.example.deezerplayer.domain.repository.ChartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChartRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val trackMapper: TrackMapper,
) : ChartRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _tracksFlow = MutableSharedFlow<List<Track>>(replay = 1)

    init {
        scope.launch {
            val initialTracks = apiService
                .getChart().tracks
                .map { trackMapper.mapTrackDtoToDomain(it) }
            _tracksFlow.emit(initialTracks)
        }
    }

    override fun getTracksFlow(): Flow<List<Track>> {
        return _tracksFlow.asSharedFlow()
    }

    override suspend fun searchTracks(query: String) {
        val tracks =
            apiService.searchTracks(query).tracks.map { trackMapper.mapTrackDtoToDomain(it) }
        _tracksFlow.emit(tracks)
    }

}
