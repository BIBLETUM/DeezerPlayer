package com.example.deezerplayer.mapper

import com.example.deezerplayer.model.TrackUi
import com.example.domain.Track
import java.util.Locale
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TrackUiMapper @Inject constructor() {

    fun mapTrackDomainToUi(domain: Track): TrackUi {
        return TrackUi(
            id = domain.id,
            title = domain.title,
            albumName = domain.albumName,
            artistName = domain.artistName,
            coverUrl = domain.coverUrl,
            duration = formatDuration(domain.length),
        )
    }

    private fun formatDuration(duration: Int): String {
        val minute = duration.toDuration(DurationUnit.SECONDS).toInt(DurationUnit.MINUTES)
        val seconds = duration.toDuration(DurationUnit.SECONDS)
            .minus(minute.toDuration(DurationUnit.MINUTES)).inWholeSeconds.toInt()
        return String.format(Locale.getDefault(), "%02d:%02d", minute, seconds)
    }

}