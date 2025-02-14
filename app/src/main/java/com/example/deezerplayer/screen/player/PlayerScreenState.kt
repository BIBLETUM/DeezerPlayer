package com.example.deezerplayer.screen.player

import com.example.deezerplayer.model.TrackUi

sealed class PlayerScreenState {
    data object Initial : PlayerScreenState()
    data class Content(
        val track: TrackUi,
        val currentProgress: Float,
        val currentDurationString: String,
        val isAudioPlaying: Boolean,
        val hasNextTrack: Boolean,
    ) : PlayerScreenState()

    data class Error(val errorMessage: String) : PlayerScreenState()
}