package com.example.data.network.mapper

import android.net.Uri
import com.example.data.network.model.SearchItemDto
import com.example.data.network.model.TrackDto
import com.example.domain.Track
import javax.inject.Inject

class RemoteTrackMapper @Inject constructor() {

    fun mapTrackDtoToDomain(dto: TrackDto): Track {
        return Track(
            id = dto.id,
            title = dto.title,
            albumName = dto.album.title,
            artistName = dto.artist.name,
            coverUrl = dto.album.cover,
            length = dto.duration,
            audioUrl = Uri.parse(dto.preview)
        )
    }

    fun mapSearchItemTrackToDomain(dto: SearchItemDto): Track {
        return Track(
            id = dto.id,
            title = dto.title,
            albumName = dto.album.title,
            artistName = dto.artist.name,
            coverUrl = dto.album.cover,
            length = dto.duration,
            audioUrl = Uri.parse(dto.preview)
        )
    }

}