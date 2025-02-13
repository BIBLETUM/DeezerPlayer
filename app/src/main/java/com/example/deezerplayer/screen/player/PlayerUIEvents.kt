package com.example.deezerplayer.screen.player

sealed class PlayerUIEvents {
    data object PlayPause : PlayerUIEvents()
    data class SelectedAudioChange(val id: Long) : PlayerUIEvents()
    data class SeekTo(val position: Float) : PlayerUIEvents()
    data object NextTrack : PlayerUIEvents()
    data object PreviousTrack : PlayerUIEvents()
    data class UpdateProgress(val newProgress: Float) : PlayerUIEvents()
}