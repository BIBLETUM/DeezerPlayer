package com.example.deezerplayer.data.network.mapper

import com.example.deezerplayer.data.network.model.TrackDto
import com.example.deezerplayer.domain.Track
import javax.inject.Inject

class TrackMapper @Inject constructor() {

    fun mapTrackDtoToDomain(trackDto: TrackDto): Track {
        return Track(
            id = trackDto.id,
            title = trackDto.title,
            albumName = trackDto.album.title,
            artistName = trackDto.artist.name,
            coverUrl = trackDto.album.cover,
        )
    }

}