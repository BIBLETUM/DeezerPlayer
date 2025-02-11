package com.example.deezerplayer.screen.track_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deezerplayer.component.LoaderScreen
import com.example.deezerplayer.component.SearchBar
import com.example.deezerplayer.component.TrackCard
import com.example.deezerplayer.model.TrackUi

@Composable
fun TrackScreenContent(
    screenState: TracksScreenState.Content,
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
private fun TrackItem(
    track: TrackUi,
    onTrackClick: (Long) -> Unit,
) {
    TrackCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        track = track,
        onTrackClick = { onTrackClick(it.id) }
    )
}