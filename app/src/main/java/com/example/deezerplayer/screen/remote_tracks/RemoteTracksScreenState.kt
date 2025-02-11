package com.example.deezerplayer.screen.remote_tracks

import com.example.deezerplayer.model.TrackUi

sealed class RemoteTracksScreenState {
    data object Initial : RemoteTracksScreenState()
    data class Content(
        val tracks: List<TrackUi>,
        val searchQuery: String,
        val isLoading: Boolean,
    ) : RemoteTracksScreenState() {
        val isError = tracks.isEmpty()
    }

    data class Error(val errorMessage: String) : RemoteTracksScreenState()
}

