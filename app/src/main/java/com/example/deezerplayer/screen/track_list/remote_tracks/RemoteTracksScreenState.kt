package com.example.deezerplayer.screen.track_list.remote_tracks

import com.example.deezerplayer.model.TrackUi
import com.example.deezerplayer.screen.track_list.TrackListContent

sealed class RemoteTracksScreenState {
    data object Initial : RemoteTracksScreenState()
    data class Content(
        override val tracks: List<TrackUi>,
        override val searchQuery: String,
        override val isLoading: Boolean,
    ) : RemoteTracksScreenState(), TrackListContent {
        override val isError = tracks.isEmpty()
    }

    data class Error(val errorMessage: String) : RemoteTracksScreenState()
}
