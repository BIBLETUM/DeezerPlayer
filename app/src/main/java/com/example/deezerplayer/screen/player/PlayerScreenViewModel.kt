package com.example.deezerplayer.screen.player

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.deezerplayer.model.TrackSourceType
import com.example.domain.use_case.IGetLastLocalTracksListUseCase
import com.example.domain.use_case.IGetLastRemoteTracksListUseCase
import javax.inject.Inject

class PlayerScreenViewModel @Inject constructor(
    private val trackId: Long,
    private val trackSourceType: TrackSourceType,
    getLastLocalTracksListUseCase: IGetLastLocalTracksListUseCase,
    getLastRemoteTracksListUseCase: IGetLastRemoteTracksListUseCase,
) : ViewModel() {

    private val lastLocalTracksList = when (trackSourceType) {
        TrackSourceType.LOCAL -> getLastLocalTracksListUseCase()
        TrackSourceType.REMOTE -> getLastRemoteTracksListUseCase()
    }

    init {
        Log.d("PlayerScreenViewModel", "trackId: $trackId, trackSourceType: $trackSourceType")
    }

}

