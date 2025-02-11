package com.example.deezerplayer.screen.track_list.remote_tracks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezerplayer.mapper.TrackUiMapper
import com.example.deezerplayer.screen.track_list.TracksScreenState
import com.example.domain.IGetRemoteTracksFlowUseCase
import com.example.domain.ISearchRemoteTracksUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemoteTracksViewModel @Inject constructor(
    private val trackMapper: TrackUiMapper,
    private val getRemoteTracksFlow: IGetRemoteTracksFlowUseCase,
    private val searchRemoteTracksUseCase: ISearchRemoteTracksUseCase,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _screenState.update { TracksScreenState.Error(throwable.toString()) }
    }

    private val _screenState =
        MutableStateFlow<TracksScreenState>(TracksScreenState.Initial)
    private val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch(exceptionHandler) {
            getRemoteTracksFlow()
                .map { tracksDomain ->
                    tracksDomain.map {
                        trackMapper.mapTrackDomainToUi(it)
                    }
                }
                .collect { tracks ->
                    Log.d("Abaoba", tracks.toString())
                    _screenState.update {
                        TracksScreenState.Content(
                            tracks = tracks,
                            searchQuery = (it as? TracksScreenState.Content)?.searchQuery
                                ?: "",
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun getScreenState(): StateFlow<TracksScreenState> = screenState

    fun selectTrack(trackId: Long) {
        Log.d("RemoteTracksViewModel", "selectTrack: $trackId")
    }

    fun searchTracks(query: String) {
        _screenState.update {
            (it as? TracksScreenState.Content)
                ?.copy(searchQuery = query, isLoading = true) ?: it
        }
        viewModelScope.launch {
            searchRemoteTracksUseCase(query)
        }
    }

}