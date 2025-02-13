package com.example.deezerplayer.player

sealed class DeezerPlayerState {
    data object Initial : DeezerPlayerState()
    data class Progress(val progress: Long) : DeezerPlayerState()
    data class Buffering(val progress: Long) : DeezerPlayerState()
    data class Playing(val isPlaying: Boolean) : DeezerPlayerState()
    data class CurrentPlaying(
        val mediaItemIndex: Int,
        val hasNextTrack: Boolean,
        val totalDurationMillis: Long,
        val currentDurationMillis: Long,
    ) : DeezerPlayerState()
}