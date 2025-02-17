package com.example.deezerplayer.player.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.deezerplayer.DeezerApplication
import com.example.deezerplayer.player.notification.DeezerAudioNotificationManager
import javax.inject.Inject

class DeezerAudioService : MediaSessionService() {

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var notificationManager: DeezerAudioNotificationManager

    override fun onCreate() {
        (applicationContext as DeezerApplication).component.inject(this)
        super.onCreate()
    }

    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.startNotificationService(
                mediaSession = mediaSession,
                mediaSessionService = this
            )
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession =
        mediaSession

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.apply {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
            }
        }
    }

    companion object {
        fun start(context: Context) {
            if (!isServiceRunning(context, DeezerAudioService::class.java)) {
                val intent = Intent(context, DeezerAudioService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            }
        }

        @Suppress("deprecation")
        private fun isServiceRunning(context: Context, serviceClass: Class<out Service>): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return manager.getRunningServices(Int.MAX_VALUE).any {
                it.service.className == serviceClass.name
            }
        }
    }

}