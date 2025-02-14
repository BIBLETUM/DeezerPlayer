package com.example.deezerplayer.player.service

import android.view.Choreographer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.deezerplayer.player.DeezerPlayerState
import com.example.deezerplayer.player.PlayerEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class DeezerAudioServiceHandler @Inject constructor(
    private val exoPlayer: ExoPlayer,
) : Player.Listener {

    private val _audioState: MutableStateFlow<DeezerPlayerState> =
        MutableStateFlow(DeezerPlayerState.Initial)
    private val audioState: StateFlow<DeezerPlayerState> = _audioState.asStateFlow()

    private val frameCallback: Choreographer.FrameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (exoPlayer.isPlaying) {
                _audioState.value = DeezerPlayerState.Progress(exoPlayer.currentPosition)
                Choreographer.getInstance().postFrameCallback(this)
            }
        }
    }

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
        selectedAudioIndex: Int = UNSPECIFIED_INDEX,
        seekPosition: Long = 0,
    ) {
        when (playerEvent) {
            PlayerEvent.PreviousTrack -> {
                if (exoPlayer.hasPreviousMediaItem()) {
                    exoPlayer.seekToPreviousMediaItem()
                } else {
                    exoPlayer.seekToDefaultPosition()
                }
            }

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
                    totalDurationMillis = exoPlayer.duration,
                    hasNextTrack = exoPlayer.hasNextMediaItem(),
                    currentDurationMillis = exoPlayer.currentPosition
                )
                _audioState.value = DeezerPlayerState.Playing(false)
            }

        }
        exoPlayer.currentPosition
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        _audioState.value = DeezerPlayerState.CurrentPlaying(
            mediaItemIndex = exoPlayer.currentMediaItemIndex,
            totalDurationMillis = exoPlayer.duration,
            hasNextTrack = exoPlayer.hasNextMediaItem(),
            currentDurationMillis = exoPlayer.currentPosition
        )
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
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    private fun stopProgressUpdate() {
        Choreographer.getInstance().removeFrameCallback(frameCallback)
    }

    companion object {
        private const val UNSPECIFIED_INDEX = -1
    }

}
