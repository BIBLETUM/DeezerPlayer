package com.example.deezerplayer.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.example.deezerplayer.player.notification.DeezerAudioNotificationManager
import com.example.deezerplayer.player.service.DeezerAudioServiceHandler
import dagger.Module
import dagger.Provides

@Module
class PlayerModule {

    @Provides
    @ApplicationScope
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    @ApplicationScope
    @UnstableApi
    fun provideExoPlayer(
        context: Context,
        audioAttributes: AudioAttributes,
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()

    @Provides
    @ApplicationScope
    fun provideMediaSession(
        context: Context,
        player: ExoPlayer,
    ): MediaSession = MediaSession.Builder(context, player).build()

    @Provides
    @ApplicationScope
    fun provideNotificationManager(
        context: Context,
        player: ExoPlayer,
    ): DeezerAudioNotificationManager = DeezerAudioNotificationManager(
        context = context,
        exoPlayer = player
    )

    @Provides
    @ApplicationScope
    fun provideServiceHandler(exoPlayer: ExoPlayer): DeezerAudioServiceHandler =
        DeezerAudioServiceHandler(exoPlayer)
}