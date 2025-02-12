package com.example.deezerplayer.screen.player

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.deezerplayer.model.TrackSourceType
import com.example.deezerplayer.player.PlayerEvent
import com.example.deezerplayer.player.service.DeezerAudioServiceHandler
import com.example.domain.use_case.IGetLastLocalTracksListUseCase
import com.example.domain.use_case.IGetLastRemoteTracksListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerScreenViewModel @Inject constructor(
    private val trackId: Long,
    private val trackSourceType: TrackSourceType,
    getLastLocalTracksListUseCase: IGetLastLocalTracksListUseCase,
    getLastRemoteTracksListUseCase: IGetLastRemoteTracksListUseCase,
    private val audioServiceHandler: DeezerAudioServiceHandler,
) : ViewModel() {

    private val tracksList = when (trackSourceType) {
        TrackSourceType.LOCAL -> getLastLocalTracksListUseCase()
        TrackSourceType.REMOTE -> getLastRemoteTracksListUseCase()
    }

    init {
        setMediaItems()
    }

    fun onUiEvents(uiEvents: PlayerUIEvents) = viewModelScope.launch {
        when (uiEvents) {
            PlayerUIEvents.Backward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            PlayerUIEvents.Forward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            PlayerUIEvents.SeekToNext -> audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            is PlayerUIEvents.PlayPause -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.PlayPause
                )
            }

            is PlayerUIEvents.SeekTo -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.SeekTo,
                    seekPosition = (2.0F).toLong()// ((duration * uiEvents.position) / 100f).toLong()
                )
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
                //  progress = uiEvents.newProgress
            }
        }
    }

    private fun setMediaItems() {
        tracksList.map { track ->
            MediaItem.Builder()
                .setUri(track.audioUrl)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(track.artistName)
                        .setArtworkUri(Uri.parse(track.coverUrl))
                        .setDisplayTitle(track.title)
                        .setSubtitle(track.albumName)
                        .build()
                )
                .build()
        }.also {
            audioServiceHandler.setMediaItemList(it)
        }
    }

}

