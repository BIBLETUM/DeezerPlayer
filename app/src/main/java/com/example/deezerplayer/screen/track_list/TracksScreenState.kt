package com.example.deezerplayer.screen.track_list

import com.example.deezerplayer.model.TrackUi

sealed class TracksScreenState {
    data object Initial : TracksScreenState()
    data class Content(
        val tracks: List<TrackUi>,
        val searchQuery: String,
        val isLoading: Boolean,
    ) : TracksScreenState() {
        val isError = tracks.isEmpty()
    }

    data class Error(val errorMessage: String) : TracksScreenState()
}

