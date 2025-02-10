package com.example.deezerplayer.data.network.mapper

import com.example.deezerplayer.data.network.model.SearchItemDto
import com.example.deezerplayer.data.network.model.TrackDto
import com.example.deezerplayer.domain.Track
import javax.inject.Inject

class TrackMapper @Inject constructor() {

    fun mapTrackDtoToDomain(dto: TrackDto): Track {
        return Track(
            id = dto.id,
            title = dto.title,
            albumName = dto.album.title,
            artistName = dto.artist.name,
            coverUrl = dto.album.cover,
        )
    }

    fun mapSearchItemTrackToDomain(dto: SearchItemDto): Track {
        return Track(
            id = dto.id,
            title = dto.title,
            albumName = dto.album.title,
            artistName = dto.artist.name,
            coverUrl = dto.album.cover,
        )
    }

}