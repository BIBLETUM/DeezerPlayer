package com.example.deezerplayer.player.service

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.deezerplayer.player.DeezerPlayerState
import com.example.deezerplayer.player.PlayerEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeezerAudioServiceHandler @Inject constructor(
    private val exoPlayer: ExoPlayer,
) : Player.Listener {

    private val _audioState: MutableStateFlow<DeezerPlayerState> =
        MutableStateFlow(DeezerPlayerState.Initial)
    private val audioState: StateFlow<DeezerPlayerState> = _audioState.asStateFlow()

    private var job: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        exoPlayer.addListener(this)
    }

    fun getAudioState(): StateFlow<DeezerPlayerState> = audioState

    fun setMediaItemList(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    fun onPlayerEvents(
        playerEvent: PlayerEvent,
        selectedAudioIndex: Int = -1,
        seekPosition: Long = 0,
    ) {
        when (playerEvent) {
            PlayerEvent.PreviousTrack -> exoPlayer.seekToPreviousMediaItem()
            PlayerEvent.NextTrack -> {
                exoPlayer.seekToNextMediaItem()
            }

            PlayerEvent.PlayPause -> playOrPause()
            PlayerEvent.SeekTo -> exoPlayer.seekTo(seekPosition)
            PlayerEvent.SelectedAudioChange -> {
                exoPlayer.seekToDefaultPosition(selectedAudioIndex)
                _audioState.value = DeezerPlayerState.Playing(
                    isPlaying = true
                )
                exoPlayer.playWhenReady = true
                startProgressUpdate()
            }

            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> {
                exoPlayer.seekTo(
                    (exoPlayer.duration * playerEvent.newProgress).toLong()
                )
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _audioState.value =
                DeezerPlayerState.Buffering(exoPlayer.currentPosition)

            ExoPlayer.STATE_READY -> {
                _audioState.value = DeezerPlayerState.CurrentPlaying(
                    mediaItemIndex = exoPlayer.currentMediaItemIndex,
                    duration = exoPlayer.duration,
                    hasNextTrack = exoPlayer.hasNextMediaItem()
                )
                _audioState.value = DeezerPlayerState.Playing(false)
            }

        }
        exoPlayer.currentPosition
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _audioState.value = DeezerPlayerState.Playing(isPlaying = isPlaying)
        if (isPlaying) {
            startProgressUpdate()
        } else {
            stopProgressUpdate()
        }
    }

    private fun playOrPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            stopProgressUpdate()
        } else {
            exoPlayer.play()
            _audioState.value = DeezerPlayerState.Playing(isPlaying = true)
            startProgressUpdate()
        }
    }

    private fun startProgressUpdate() {
        job?.cancel()
        job = coroutineScope.launch {
            while (isActive) {
                delay(500)
                _audioState.value = DeezerPlayerState.Progress(exoPlayer.currentPosition)
            }
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _audioState.value = DeezerPlayerState.Playing(isPlaying = false)
    }
}
