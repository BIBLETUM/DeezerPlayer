package com.example.deezerplayer.domain

data class Track(
    val id: Long,
    val title: String,
    val albumName: String,
    val artistName: String,
    val coverUrl: String,
)