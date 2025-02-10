package com.example.deezerplayer.presentation.screen.remote_tracks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezerplayer.domain.IGetRemoteTracksUseCase
import com.example.deezerplayer.domain.ISearchRemoteTracksUseCase
import com.example.deezerplayer.presentation.mapper.TrackUiMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemoteTracksViewModel @Inject constructor(
    private val trackMapper: TrackUiMapper,
    private val getRemoteTracksUseCase: IGetRemoteTracksUseCase,
    private val searchRemoteTracksUseCase: ISearchRemoteTracksUseCase,
) : ViewModel() {

    private val _screenState =
        MutableStateFlow<RemoteTracksScreenState>(RemoteTracksScreenState.Initial)
    private val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            getRemoteTracksUseCase()
                .map { tracksDomain ->
                    tracksDomain.map {
                        trackMapper.mapTrackDomainToUi(it)
                    }
                }
                .collect { tracks ->
                    _screenState.update {
                        RemoteTracksScreenState.Content(
                            tracks = tracks,
                            searchQuery = (it as? RemoteTracksScreenState.Content)?.searchQuery
                                ?: "",
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun getScreenState(): StateFlow<RemoteTracksScreenState> = screenState

    fun selectTrack(trackId: Long) {
        Log.d("RemoteTracksViewModel", "selectTrack: $trackId")
    }

    fun searchTracks(query: String) {
        _screenState.update {
            (it as? RemoteTracksScreenState.Content)
                ?.copy(searchQuery = query, isLoading = true) ?: it
        }
        viewModelScope.launch {
            searchRemoteTracksUseCase(query)
        }
    }

}