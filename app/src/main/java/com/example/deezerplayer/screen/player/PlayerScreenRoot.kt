package com.example.deezerplayer.screen.player

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    onComposing: () -> Unit,
    paddingValues: PaddingValues,
) {
    LaunchedEffect(Unit) {
        onComposing()
    }
    val component = getApplicationComponent()
        .getPlayerScreenComponentFactory()
        .create(trackId, trackSourceType)
    val factory = component.getViewModelFactory()
    val viewModel: PlayerScreenViewModel = viewModel(
        factory = factory
    )
    val context = LocalContext.current
    SideEffect {
        startAudioService(context, DeezerAudioService::class.java)
        viewModel.onUiEvents(PlayerUIEvents.SelectedAudioChange(trackId))
    }
}

private fun startAudioService(context: Context, serviceClass: Class<*>) {
    if (!isServiceRunning(context, serviceClass)) {
        val intent = Intent(context, serviceClass)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}

@SuppressWarnings("deprecation")
private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (service.service.className == serviceClass.name) {
            return true
        }
    }
    return false
}