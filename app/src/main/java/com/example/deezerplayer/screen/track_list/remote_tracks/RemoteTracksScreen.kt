package com.example.deezerplayer.screen.track_list.remote_tracks

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deezerplayer.component.LoaderScreen
import com.example.deezerplayer.getApplicationComponent
import com.example.deezerplayer.screen.track_list.TrackScreenContent
import com.example.deezerplayer.screen.track_list.TracksScreenState

@Composable
fun RemoteTracksScreenRoot() {
    val component = getApplicationComponent()
    val viewModel: RemoteTracksViewModel = viewModel(factory = component.getViewModelFactory())
    val state = viewModel.getScreenState().collectAsStateWithLifecycle()

    RemoteTracksScreen(
        state = state,
        onQueryChange = viewModel::searchTracks,
        onTrackClick = viewModel::selectTrack,
    )
}

@Composable
private fun RemoteTracksScreen(
    state: State<TracksScreenState>,
    onQueryChange: (String) -> Unit,
    onTrackClick: (Long) -> Unit,
) {

    when (val currentState = state.value) {
        is TracksScreenState.Content -> {
            TrackScreenContent(
                screenState = currentState,
                onQueryChange = onQueryChange,
                onTrackClick = onTrackClick,
            )
        }

        TracksScreenState.Initial -> {
            LoaderScreen()
        }

        is TracksScreenState.Error -> Log.d("RemoteTracksScreen", currentState.errorMessage)
    }

}