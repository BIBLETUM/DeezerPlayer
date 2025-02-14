package com.example.deezerplayer.model

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class TrackUi(
    val id: Long,
    val title: String,
    val albumName: String,
    val artistName: String,
    val coverUrl: String,
    val durationString: String,
    val durationSeconds: Int,
    val audioUri: Uri,
)