package com.example.deezerplayer.mapper

import com.example.deezerplayer.model.TrackUi
import com.example.domain.Track
import javax.inject.Inject

class TrackUiMapper @Inject constructor() {

    fun mapTrackDomainToUi(domain: Track): TrackUi {
        return TrackUi(
            id = domain.id,
            title = domain.title,
            albumName = domain.albumName,
            artistName = domain.artistName,
            coverUrl = domain.coverUrl,
            length = domain.length,
        )
    }

}