package com.example.deezerplayer.screen.track_list

import com.example.deezerplayer.model.TrackUi

interface TrackListContent {

    val tracks: List<TrackUi>
    val searchQuery: String
    val isLoading: Boolean
    val isError: Boolean

}