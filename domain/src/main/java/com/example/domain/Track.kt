package com.example.domain

data class Track(
    val id: Long,
    val title: String,
    val albumName: String,
    val artistName: String,
    val coverUrl: String,
    val length: Int,
)