package com.example.deezerplayer.screen.track_list.remote_tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deezerplayer.mapper.TrackUiMapper
import com.example.domain.use_case.IGetRemoteTracksFlowUseCase
import com.example.domain.use_case.ISearchRemoteTracksUseCase
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
        _screenState.update { RemoteTracksScreenState.Error(throwable.toString()) }
    }

    private val _screenState =
        MutableStateFlow<RemoteTracksScreenState>(RemoteTracksScreenState.Initial)
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