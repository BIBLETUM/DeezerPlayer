package com.example.deezerplayer.presentation.screen.remote_tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deezerplayer.presentation.component.LoaderScreen
import com.example.deezerplayer.presentation.component.SearchBar
import com.example.deezerplayer.presentation.component.TrackCard
import com.example.deezerplayer.presentation.getApplicationComponent
import com.example.deezerplayer.presentation.model.TrackUi

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
    state: State<RemoteTracksScreenState>,
    onQueryChange: (String) -> Unit,
    onTrackClick: (Long) -> Unit,
) {

    when (val currentState = state.value) {
        is RemoteTracksScreenState.Content -> {
            RemoteTrackScreenContent(
                screenState = currentState,
                onQueryChange = onQueryChange,
                onTrackClick = onTrackClick,
            )
        }

        RemoteTracksScreenState.Initial -> {
            LoaderScreen()
        }
    }

}

@Composable
private fun RemoteTrackScreenContent(
    screenState: RemoteTracksScreenState.Content,
    onQueryChange: (String) -> Unit,
    onTrackClick: (Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SearchBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp),
                text = screenState.searchQuery,
                isError = screenState.isError,
                onQueryChange = onQueryChange,
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        when (screenState.isLoading) {
            true -> {
                item {
                    LoaderScreen(
                        modifier = Modifier.padding(top = 200.dp)
                    )
                }
            }

            else -> {
                items(items = screenState.tracks, key = { it.id }) { track ->
                    TrackItem(track = track, onTrackClick = onTrackClick)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun LazyItemScope.TrackItem(
    track: TrackUi,
    onTrackClick: (Long) -> Unit,
) {
    TrackCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        track = track,
        onTrackClick = { onTrackClick(it.id) }
    )
}