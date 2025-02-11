package com.example.domain

import android.net.Uri

data class Track(
    val id: Long,
    val title: String,
    val albumName: String,
    val artistName: String,
    val coverUrl: String,
    val length: Int,
    val audioUrl: Uri,
)