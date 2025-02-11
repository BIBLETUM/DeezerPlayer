package com.example.data.local.model

data class MusicFile(
    val id: Long,
    val title: String,
    val artistName: String,
    val albumName: String,
    val albumCover: String?,
    val lengthInMillis: Long,
)