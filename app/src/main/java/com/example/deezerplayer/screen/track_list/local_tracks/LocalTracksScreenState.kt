package com.example.deezerplayer.screen.track_list.local_tracks

import com.example.deezerplayer.model.TrackUi
import com.example.deezerplayer.screen.track_list.TrackListContent

sealed class LocalTracksScreenState {
    data object Initial : LocalTracksScreenState()
    data class Content(
        override val tracks: List<TrackUi>,
        override val searchQuery: String,
        override val isLoading: Boolean,
    ) : LocalTracksScreenState(), TrackListContent {
        override val isError = tracks.isEmpty()
    }

    data class Error(val errorMessage: String) : LocalTracksScreenState()
    data object MissingPermission : LocalTracksScreenState()
}

