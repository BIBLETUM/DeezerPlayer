package com.example.deezerplayer.screen.player

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deezerplayer.getApplicationComponent
import com.example.deezerplayer.model.TrackSourceType
import com.example.deezerplayer.player.service.DeezerAudioService

@Composable
fun PlayerScreenRoot(
    trackId: Long,
    trackSourceType: TrackSourceType,
    paddingValues: PaddingValues,
) {
    val component = getApplicationComponent()
        .getPlayerScreenComponentFactory()
        .create(trackId, trackSourceType)
    val factory = component.getViewModelFactory()
    val viewModel: PlayerScreenViewModel = viewModel(
        factory = factory
    )
    val context = LocalContext.current
    SideEffect {
        if (!isServiceRunning(context, DeezerAudioService::class.java)) {
            val intent = Intent(context, DeezerAudioService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        viewModel.onUiEvents(PlayerUIEvents.SelectedAudioChange(trackId))
    }
}