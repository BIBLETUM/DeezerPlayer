package com.example.deezerplayer.screen.track_list.local_tracks

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deezerplayer.R
import com.example.deezerplayer.component.LoaderScreen
import com.example.deezerplayer.getApplicationComponent
import com.example.deezerplayer.model.TrackUi
import com.example.deezerplayer.permission.RequestMediaAudioPermission
import com.example.deezerplayer.permission.openAppSettings
import com.example.deezerplayer.screen.track_list.TrackScreenContent
import com.example.deezerplayer.theme.nunitoFontFamily

@Composable
fun LocalTracksScreenRoot(
    paddingValues: PaddingValues,
    navigateToPlayer: (Long, String) -> Unit,
) {
    val component = getApplicationComponent()
    val viewModel: LocalTracksViewModel = viewModel(factory = component.getViewModelFactory())
    val state = viewModel.getScreenState().collectAsStateWithLifecycle()

    RequestMediaAudioPermission(
        onPermissionGranted = viewModel::loadData,
        onPermissionDismissed = {
            viewModel.forbidAudioPermission()
            openAppSettings(it)
        },
    )

    LocalTracksScreen(
        paddingValues = paddingValues,
        state = state,
        onQueryChange = viewModel::searchTracks,
        onTrackClick = navigateToPlayer,
    )
}

@Composable
private fun LocalTracksScreen(
    paddingValues: PaddingValues,
    state: State<LocalTracksScreenState>,
    onQueryChange: (String) -> Unit,
    onTrackClick: (Long, String) -> Unit,
) {

    when (val currentState = state.value) {
        is LocalTracksScreenState.Content -> {
            TrackScreenContent(
                paddingValues = paddingValues,
                screenState = currentState,
                onQueryChange = onQueryChange,
                onTrackClick = {
                    onTrackClick(it, TrackUi.SOURCE_LOCAL)
                },
            )
        }

        LocalTracksScreenState.Initial -> {
            LoaderScreen()
        }

        is LocalTracksScreenState.Error -> Log.d("LocalTracksScreenRoot", currentState.errorMessage)
        LocalTracksScreenState.MissingPermission -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.media_audio_permission_missing),
                    style = TextStyle(
                        fontFamily = nunitoFontFamily,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = stringResource(R.string.if_you_gave_permission_try_restarting_the_application),
                    style = TextStyle(
                        fontFamily = nunitoFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }

}