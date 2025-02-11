package com.example.deezerplayer.model

import androidx.compose.runtime.Immutable

@Immutable
data class TrackUi(
    val id: Long,
    val title: String,
    val albumName: String,
    val artistName: String,
    val coverUrl: String,
    val length: Int,
) {
    companion object {
        const val SOURCE_REMOTE = "remote"
        const val SOURCE_LOCAL = "local"
    }
}