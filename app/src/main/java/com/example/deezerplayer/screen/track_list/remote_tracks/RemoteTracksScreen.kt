package com.example.deezerplayer.screen.track_list.remote_tracks

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deezerplayer.component.LoaderScreen
import com.example.deezerplayer.getApplicationComponent
import com.example.deezerplayer.model.TrackSourceType
import com.example.deezerplayer.screen.track_list.TrackScreenContent

@Composable
fun RemoteTracksScreenRoot(
    paddingValues: PaddingValues,
    navigateToPlayer: (Long, TrackSourceType) -> Unit,
) {
    val component = getApplicationComponent()
    val viewModel: RemoteTracksViewModel = viewModel(factory = component.getViewModelFactory())
    val state = viewModel.getScreenState().collectAsStateWithLifecycle()

    RemoteTracksScreen(
        paddingValues = paddingValues,
        state = state,
        onQueryChange = viewModel::searchTracks,
        onTrackClick = { trackId ->
            navigateToPlayer(trackId, TrackSourceType.REMOTE)
        },
    )
}

@Composable
private fun RemoteTracksScreen(
    paddingValues: PaddingValues,
    state: State<RemoteTracksScreenState>,
    onQueryChange: (String) -> Unit,
    onTrackClick: (Long) -> Unit,
) {

    when (val currentState = state.value) {
        is RemoteTracksScreenState.Content -> {
            TrackScreenContent(
                paddingValues = paddingValues,
                screenState = currentState,
                onQueryChange = onQueryChange,
                onTrackClick = onTrackClick,
            )
        }

        RemoteTracksScreenState.Initial -> {
            LoaderScreen()
        }

        is RemoteTracksScreenState.Error -> Log.d("RemoteTracksScreen", currentState.errorMessage)
    }

}