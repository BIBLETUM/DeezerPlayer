package com.example.deezerplayer.screen.player

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.deezerplayer.R
import com.example.deezerplayer.component.LoaderScreen
import com.example.deezerplayer.getApplicationComponent
import com.example.deezerplayer.model.TrackSourceType
import com.example.deezerplayer.player.service.DeezerAudioService
import com.example.deezerplayer.theme.PlayerTheme
import com.example.deezerplayer.theme.nunitoFontFamily

@Composable
fun PlayerScreenRoot(
    trackId: Long,
    trackSourceType: TrackSourceType,
    onComposing: () -> Unit,
    paddingValues: PaddingValues,
    navigateBack: () -> Unit,
) {
    LaunchedEffect(Unit) {
        onComposing()
    }
    val component = getApplicationComponent()
        .getPlayerScreenComponentFactory()
        .create(trackSourceType)
    val factory = component.getViewModelFactory()
    val viewModel: PlayerScreenViewModel = viewModel(
        factory = factory
    )

    val screenState = viewModel.getScreenState().collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        DeezerAudioService.start(context)
        viewModel.onUiEvents(PlayerUIEvents.SelectedAudioChange(trackId))
    }
    PlayerScreen(
        paddingValues = paddingValues,
        screenState = screenState,
        onProgress = { viewModel.onUiEvents(PlayerUIEvents.SeekTo(it)) },
        onPlayPause = { viewModel.onUiEvents(PlayerUIEvents.PlayPause) },
        onNext = { viewModel.onUiEvents(PlayerUIEvents.NextTrack) },
        onPrevious = { viewModel.onUiEvents(PlayerUIEvents.PreviousTrack) },
        navigateBack = navigateBack,
    )
}

@Composable
private fun PlayerScreen(
    paddingValues: PaddingValues,
    screenState: State<PlayerScreenState>,
    onProgress: (Float) -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    navigateBack: () -> Unit,
) {
    when (val currentState = screenState.value) {
        is PlayerScreenState.Content -> PlayerScreenContent(
            paddingValues = paddingValues,
            screenState = currentState,
            onProgress = onProgress,
            onPlayPause = onPlayPause,
            onNext = onNext,
            onPrevious = onPrevious,
            navigateBack = navigateBack
        )

        is PlayerScreenState.Error -> Log.d("PlayerScreen", currentState.errorMessage)
        PlayerScreenState.Initial -> LoaderScreen()
    }
}

@Composable
private fun PlayerScreenContent(
    paddingValues: PaddingValues,
    screenState: PlayerScreenState.Content,
    onProgress: (Float) -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    navigateBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 26.dp)
            .padding(paddingValues),
    ) {
        TopBar(navigateBack = navigateBack)
        Spacer(Modifier.height(20.dp))
        AlbumCover(coverUrl = screenState.track.coverUrl)
        Spacer(Modifier.height(18.dp))
        TrackDescription(
            trackName = screenState.track.title,
            artistName = screenState.track.artistName,
            albumName = screenState.track.albumName
        )
        Spacer(modifier = Modifier.height(20.dp))
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = screenState.currentProgress,
            colors = SliderDefaults.colors(
                thumbColor = PlayerTheme.colors.primary,
                activeTrackColor = PlayerTheme.colors.primary,
            ),
            onValueChange = { onProgress(it) },
            valueRange = 0f..100f
        )
        DurationInfo(
            modifier = Modifier.fillMaxWidth(),
            durationCurrent = screenState.currentDurationString,
            durationTotal = screenState.track.durationString
        )
        Spacer(Modifier.height(68.dp))
        AudioControls(
            modifier = Modifier.fillMaxWidth(),
            isPlaying = screenState.isAudioPlaying,
            isNextEnabled = screenState.hasNextTrack,
            onPlayPause = onPlayPause,
            onNext = onNext,
            onPrevious = onPrevious,
        )
    }
}

@Composable
private fun AudioControls(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    isNextEnabled: Boolean,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
) {
    val iconPlayPauseResId = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NextPreviousButton(
            iconResId = R.drawable.ic_scip_back,
            onClick = onPrevious
        )

        Box(
            modifier = Modifier
                .size(72.dp)
                .shadow(10.dp, CircleShape)
                .clickable(onClick = onPlayPause)
                .background(PlayerTheme.colors.primary), contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(46.dp),
                painter = painterResource(id = iconPlayPauseResId),
                contentDescription = null,
                tint = Color.White
            )
        }

        NextPreviousButton(
            iconResId = R.drawable.ic_scip_next,
            onClick = onNext,
            enabled = isNextEnabled,
            tint = if (isNextEnabled) Color.Black else Color.Black.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun NextPreviousButton(
    modifier: Modifier = Modifier,
    iconResId: Int,
    tint: Color = Color.Black,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick, enabled = enabled),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
private fun DurationInfo(
    modifier: Modifier = Modifier,
    durationCurrent: String,
    durationTotal: String,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = durationCurrent,
            style = TextStyle(
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
        Text(
            text = durationTotal,
            style = TextStyle(
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
    }
}

@Composable
private fun TrackDescription(
    modifier: Modifier = Modifier,
    trackName: String,
    artistName: String,
    albumName: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = trackName,
            style = TextStyle(
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
        Text(
            text = artistName,
            style = TextStyle(
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
        Text(
            text = albumName,
            style = TextStyle(
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
    }
}

@Composable
private fun ColumnScope.AlbumCover(
    modifier: Modifier = Modifier,
    coverUrl: String,
) {
    AsyncImage(
        modifier = modifier
            .align(Alignment.CenterHorizontally)
            .height(370.dp)
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(30.dp)),
        model = coverUrl,
        placeholder = painterResource(id = R.drawable.track_image_placeholder),
        error = painterResource(id = R.drawable.track_image_placeholder),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(vertical = 14.dp)
            .fillMaxWidth()
            .background(PlayerTheme.colors.neutralWhite)
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(100))
                .background(PlayerTheme.colors.backgroundDisabled)
                .clickable {
                    navigateBack()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(14.dp),
                painter = painterResource(id = R.drawable.arrow_back_icon),
                contentDescription = null,
                tint = Color.Black
            )
        }
        Text(
            modifier = modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.now_playing),
            style = TextStyle(
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            ),
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(36.dp))
    }
}