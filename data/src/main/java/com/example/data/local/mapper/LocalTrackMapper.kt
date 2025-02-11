package com.example.data.local.mapper

import com.example.data.local.model.MusicFile
import com.example.domain.Track
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocalTrackMapper @Inject constructor() {

    fun mapMusicFileToDomain(local: MusicFile): Track {
        return Track(
            id = local.id,
            title = local.title,
            albumName = local.albumName,
            artistName = local.artistName,
            coverUrl = local.albumCover ?: "",
            length = TimeUnit.MILLISECONDS.toSeconds(local.lengthInMillis).toInt(),
        )
    }

}