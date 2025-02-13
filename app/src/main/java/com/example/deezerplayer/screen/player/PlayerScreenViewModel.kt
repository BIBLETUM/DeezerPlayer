package com.example.deezerplayer.screen.player

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.deezerplayer.mapper.TrackUiMapper
import com.example.deezerplayer.model.TrackSourceType
import com.example.deezerplayer.model.TrackUi
import com.example.deezerplayer.player.DeezerPlayerState
import com.example.deezerplayer.player.PlayerEvent
import com.example.deezerplayer.player.service.DeezerAudioServiceHandler
import com.example.domain.use_case.IGetLastLocalTracksListUseCase
import com.example.domain.use_case.IGetLastRemoteTracksListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class PlayerScreenViewModel @Inject constructor(
    trackSourceType: TrackSourceType,
    private val trackMapper: TrackUiMapper,
    getLastLocalTracksListUseCase: IGetLastLocalTracksListUseCase,
    getLastRemoteTracksListUseCase: IGetLastRemoteTracksListUseCase,
    private val audioServiceHandler: DeezerAudioServiceHandler,
) : ViewModel() {

    private val tracksList = when (trackSourceType) {
        TrackSourceType.LOCAL -> getLastLocalTracksListUseCase()
        TrackSourceType.REMOTE -> getLastRemoteTracksListUseCase()
    }.run {
        map { trackMapper.mapTrackDomainToUi(it) }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _screenState.update { PlayerScreenState.Error(throwable.toString()) }
    }

    private val _screenState: MutableStateFlow<PlayerScreenState> =
        MutableStateFlow(PlayerScreenState.Initial)
    private val screenState: StateFlow<PlayerScreenState> = _screenState.asStateFlow()

    init {
        setMediaItems()
        collectServiceState()
    }

    fun getScreenState(): StateFlow<PlayerScreenState> = screenState

    fun onUiEvents(uiEvents: PlayerUIEvents) {
        viewModelScope.launch(exceptionHandler) {
            when (uiEvents) {
                PlayerUIEvents.PreviousTrack -> audioServiceHandler.onPlayerEvents(PlayerEvent.PreviousTrack)
                PlayerUIEvents.NextTrack -> audioServiceHandler.onPlayerEvents(PlayerEvent.NextTrack)
                is PlayerUIEvents.PlayPause -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.PlayPause
                    )
                }

                is PlayerUIEvents.SeekTo -> {
                    getCurrentContent()?.let { currentContent ->
                        val durationMs = currentContent.track.duration * 1000L
                        val seekPosition = ((durationMs * uiEvents.position) / 100f).toLong()
                        audioServiceHandler.onPlayerEvents(
                            PlayerEvent.SeekTo,
                            seekPosition = seekPosition
                        )
                    }
                }

                is PlayerUIEvents.SelectedAudioChange -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.SelectedAudioChange,
                        selectedAudioIndex = tracksList.indexOfFirst { it.id == uiEvents.id }
                    )
                }

                is PlayerUIEvents.UpdateProgress -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.UpdateProgress(
                            uiEvents.newProgress
                        )
                    )
                    updateScreenStateProgress(uiEvents.newProgress)
                }
            }
        }
    }

    private fun collectServiceState() {
        viewModelScope.launch(exceptionHandler) {
            audioServiceHandler.getAudioState().collectLatest { mediaState ->
                when (mediaState) {
                    DeezerPlayerState.Initial -> _screenState.update { PlayerScreenState.Initial }
                    is DeezerPlayerState.Buffering -> calculateProgressValue(mediaState.progress)
                    is DeezerPlayerState.Playing -> {
                        Log.d("Playing", "ABBA")
                        getCurrentContent()?.let { currentState ->
                            _screenState.update {
                                currentState.copy(isAudioPlaying = mediaState.isPlaying)
                            }
                        }
                    }

                    is DeezerPlayerState.Progress -> calculateProgressValue(mediaState.progress)
                    is DeezerPlayerState.CurrentPlaying -> {
                        Log.d("CurrentPlaying", "ABBA")
                        val track = tracksList[mediaState.mediaItemIndex]
                        updatePlayingSong(
                            actualDuration = mediaState.duration,
                            track = track,
                            hasNextTrack = mediaState.hasNextTrack
                        )
                    }
                }
            }
        }
    }

    private fun updatePlayingSong(track: TrackUi, actualDuration: Long, hasNextTrack: Boolean) {
        _screenState.update {
            PlayerScreenState.Content(
                track = track.copy(
                    duration = actualDuration.toDuration(DurationUnit.MILLISECONDS).inWholeSeconds.toInt(),
                    durationString = formatDuration(actualDuration)
                ),
                progress = INITIAL_PROGRESS,
                durationCurrent = INITIAL_PROGRESS_STRING,
                isAudioPlaying = true,
                hasNextTrack = hasNextTrack,
            )
        }
    }

    private fun calculateProgressValue(currentProgress: Long) {
        val currentState = getCurrentContent() ?: return
        _screenState.update {
            currentState.copy(
                durationCurrent = formatDuration(currentProgress),
                progress = if (currentProgress > 0) {
                    ((currentProgress.toFloat() / (currentState.track.duration.toFloat() * 1000f)) * 100f)
                } else {
                    0f
                }
            )
        }
    }

    private fun updateScreenStateProgress(newProgress: Float) {
        val currentContent = getCurrentContent() ?: return
        _screenState.update {
            currentContent.copy(progress = newProgress)
        }
    }

    private fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minute, seconds)
    }

    private fun getCurrentContent(): PlayerScreenState.Content? {
        return (_screenState.value as? PlayerScreenState.Content)
    }

    private fun setMediaItems() {
        tracksList.takeIf { it.isNotEmpty() }?.map { track ->
            MediaItem.Builder()
                .setUri(track.audioUri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(track.artistName)
                        .setArtworkUri(Uri.parse(track.coverUrl))
                        .setDisplayTitle(track.title)
                        .setSubtitle(track.albumName)
                        .build()
                )
                .build()
        }?.let {
            audioServiceHandler.setMediaItemList(it)
        }
    }

    private companion object {
        const val INITIAL_PROGRESS_STRING = "00:00"

        const val INITIAL_PROGRESS = 0f
    }

}

