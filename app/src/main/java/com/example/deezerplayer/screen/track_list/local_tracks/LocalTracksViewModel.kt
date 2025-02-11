package com.example.deezerplayer.screen.track_list.local_tracks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezerplayer.mapper.TrackUiMapper
import com.example.domain.use_case.IGetLocalTracksFlowUseCase
import com.example.domain.use_case.ISearchLocalTracksUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalTracksViewModel @Inject constructor(
    private val trackMapper: TrackUiMapper,
    private val getTracksFlow: IGetLocalTracksFlowUseCase,
    private val searchTracksUseCase: ISearchLocalTracksUseCase,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _screenState.update { LocalTracksScreenState.Error(throwable.toString()) }
    }

    private val _screenState =
        MutableStateFlow<LocalTracksScreenState>(LocalTracksScreenState.Initial)
    private val screenState = _screenState.asStateFlow()

    fun loadData() {
        viewModelScope.launch(exceptionHandler) {
            getTracksFlow()
                .map { tracksDomain ->
                    tracksDomain.map {
                        trackMapper.mapTrackDomainToUi(it)
                    }
                }
                .collect { tracks ->
                    _screenState.update {
                        LocalTracksScreenState.Content(
                            tracks = tracks,
                            searchQuery = (it as? LocalTracksScreenState.Content)?.searchQuery
                                ?: "",
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun getScreenState(): StateFlow<LocalTracksScreenState> = screenState

    fun selectTrack(trackId: Long) {
        Log.d("RemoteTracksViewModel", "selectTrack: $trackId")
    }

    fun forbidAudioPermission() {
        _screenState.update { LocalTracksScreenState.MissingPermission }
    }

    fun searchTracks(query: String) {
        _screenState.update {
            (it as? LocalTracksScreenState.Content)
                ?.copy(searchQuery = query, isLoading = true) ?: it
        }
        viewModelScope.launch {
            searchTracksUseCase(query)
        }
    }

}