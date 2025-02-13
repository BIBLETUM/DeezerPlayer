package com.example.deezerplayer.screen.player

import com.example.deezerplayer.model.TrackUi

sealed class PlayerScreenState {
    data object Initial : PlayerScreenState()
    data class Content(
        val track: TrackUi,
        val progress: Float,
        val isAudioPlaying: Boolean,
    ) : PlayerScreenState()
}