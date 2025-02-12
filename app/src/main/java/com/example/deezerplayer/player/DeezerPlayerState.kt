package com.example.deezerplayer.player

sealed class DeezerPlayerState {
    data object Initial : DeezerPlayerState()
    data class Ready(val duration: Long) : DeezerPlayerState()
    data class Progress(val progress: Long) : DeezerPlayerState()
    data class Buffering(val progress: Long) : DeezerPlayerState()
    data class Playing(val isPlaying: Boolean) : DeezerPlayerState()
    data class CurrentPlaying(val mediaItemIndex: Int) : DeezerPlayerState()
}