package com.example.data.network

import com.example.data.network.mapper.RemoteTrackMapper
import com.example.domain.Track
import com.example.domain.repository.ChartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemoteChartRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val remoteTrackMapper: RemoteTrackMapper,
) : ChartRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _tracksFlow = MutableSharedFlow<List<Track>>(replay = 1)

    init {
        scope.launch {
            val initialTracks = apiService.getChart().trackList.tracks
                .filter { it.type == TRACK_TYPE }
                .map { remoteTrackMapper.mapTrackDtoToDomain(it) }
            _tracksFlow.emit(initialTracks)
        }
    }

    override fun getTracksFlow(): Flow<List<Track>> {
        return _tracksFlow.asSharedFlow()
    }

    override suspend fun searchTracks(query: String) {
        val tracks = when (query.length) {
            0 -> {
                apiService.getChart().trackList.tracks
                    .filter { it.type == TRACK_TYPE }
                    .map { remoteTrackMapper.mapTrackDtoToDomain(it) }
            }

            else -> {
                apiService.searchTracks(query).dataList
                    .filter { it.type == TRACK_TYPE }
                    .map { remoteTrackMapper.mapSearchItemTrackToDomain(it) }
            }
        }
        _tracksFlow.emit(tracks)
    }

    private companion object {
        const val TRACK_TYPE = "track"
    }

}
