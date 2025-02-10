package com.example.deezerplayer.presentation.screen.remote_tracks

import com.example.deezerplayer.presentation.model.TrackUi

sealed class RemoteTracksScreenState {
    data object Initial : RemoteTracksScreenState()
    data class Content(
        val tracks: List<TrackUi>,
        val searchQuery: String,
        val isLoading: Boolean,
    ) : RemoteTracksScreenState() {
        val isError = tracks.isEmpty()
    }
}

