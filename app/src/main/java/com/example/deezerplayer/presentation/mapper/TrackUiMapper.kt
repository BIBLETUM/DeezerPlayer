package com.example.deezerplayer.presentation.mapper

import com.example.deezerplayer.domain.Track
import com.example.deezerplayer.presentation.model.TrackUi
import javax.inject.Inject

class TrackUiMapper @Inject constructor() {

    fun mapTrackDomainToUi(domain: Track): TrackUi {
        return TrackUi(
            id = domain.id,
            title = domain.title,
            albumName = domain.albumName,
            artistName = domain.artistName,
            coverUrl = domain.coverUrl,
        )
    }

}