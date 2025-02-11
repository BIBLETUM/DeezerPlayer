package com.example.deezerplayer.screen.player

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deezerplayer.getApplicationComponent

@Composable
fun PlayerScreenRoot(
    trackId: Long,
    trackSourceType: String,
    paddingValues: PaddingValues,
) {
    val component = getApplicationComponent()
        .getPlayerScreenComponentFactory()
        .create(trackId, trackSourceType)
    val factory = component.getViewModelFactory()
    val viewModel: PlayerScreenViewModel = viewModel(
        factory = factory
    )
    viewModel.toString()
}