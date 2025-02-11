package com.example.deezerplayer.screen.player

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class PlayerScreenViewModel @Inject constructor(
    private val trackId: Long,
    private val trackSourceType: String,
) : ViewModel() {

    init {
        Log.d("PlayerScreenViewModel", "trackId: $trackId, trackSourceType: $trackSourceType")
    }

}

