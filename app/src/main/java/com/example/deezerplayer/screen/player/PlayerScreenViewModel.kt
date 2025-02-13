package com.example.deezerplayer.screen.player

import android.net.Uri
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
                        val durationMs = currentContent.track.durationSeconds
                            .toDuration(DurationUnit.SECONDS)
                            .inWholeMilliseconds
                            .toFloat()
                        val seekPosition = ((durationMs * uiEvents.position) / PERCANTAGE).toLong()
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
                        getCurrentContent()?.let { currentState ->
                            _screenState.update {
                                currentState.copy(isAudioPlaying = mediaState.isPlaying)
                            }
                        }
                    }

                    is DeezerPlayerState.Progress -> calculateProgressValue(mediaState.progress)
                    is DeezerPlayerState.CurrentPlaying -> {
                        val track = tracksList[mediaState.mediaItemIndex]
                        updatePlayingSong(
                            totalDurationMillis = mediaState.totalDurationMillis,
                            track = track,
                            hasNextTrack = mediaState.hasNextTrack,
                            currentDurationMillis = mediaState.currentDurationMillis
                        )
                    }
                }
            }
        }
    }

    private fun updatePlayingSong(
        track: TrackUi,
        totalDurationMillis: Long,
        currentDurationMillis: Long,
        hasNextTrack: Boolean
    ) {
        _screenState.update {
            PlayerScreenState.Content(
                track = track.copy(
                    durationSeconds = totalDurationMillis.toDuration(DurationUnit.MILLISECONDS).inWholeSeconds.toInt(),
                    durationString = formatDuration(totalDurationMillis)
                ),
                currentProgress = getCurrentProgress(currentDurationMillis, totalDurationMillis),
                currentDurationString = formatDuration(currentDurationMillis),
                isAudioPlaying = true,
                hasNextTrack = hasNextTrack,
            )
        }
    }

    private fun calculateProgressValue(currentProgressMillis: Long) {
        val currentState = getCurrentContent() ?: return
        _screenState.update {
            currentState.copy(
                currentDurationString = formatDuration(currentProgressMillis),
                currentProgress = getCurrentProgress(
                    currentDurationMillis = currentProgressMillis,
                    totalDurationMillis = (currentState.track.durationSeconds * 1000f).toLong()
                )
            )
        }
    }

    private fun getCurrentProgress(currentDurationMillis: Long, totalDurationMillis: Long): Float {
        return if (currentDurationMillis > 0) {
            ((currentDurationMillis.toFloat() / (totalDurationMillis.toFloat())) * PERCANTAGE)
        } else {
            INITIAL_PROGRESS
        }
    }

    private fun updateScreenStateProgress(newProgress: Float) {
        val currentContent = getCurrentContent() ?: return
        _screenState.update {
            currentContent.copy(currentProgress = newProgress)
        }
    }

    private fun formatDuration(durationMillis: Long): String {
        val minute = TimeUnit.MINUTES.convert(durationMillis, TimeUnit.MILLISECONDS)
        val seconds =
            TimeUnit.SECONDS.convert(durationMillis, TimeUnit.MILLISECONDS) % SECONDS_IN_MINUTE
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
        const val PERCANTAGE = 100f
        const val INITIAL_PROGRESS = 0f
        const val SECONDS_IN_MINUTE = 60
    }

}

