package com.example.deezerplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class AudioPlayerService : LifecycleService() {

    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PLAY -> playAudio(intent.getStringExtra(EXTRA_AUDIO_URL))
            ACTION_PAUSE -> pauseAudio()
        }
        return START_STICKY
    }

    private fun playAudio(audioUrl: String?) {
        if (audioUrl.isNullOrEmpty()) return

        val mediaItem = MediaItem.fromUri(audioUrl)
        exoPlayer.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
        startForeground(NOTIFICATION_ID, createNotification())
    }

    private fun pauseAudio() {
        exoPlayer.pause()
        stopForeground(false)
    }

    private fun createNotification(): Notification {
        val channelId = "audio_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Audio Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Audio Player")
            .setSmallIcon(R.drawable.search_icon)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val EXTRA_AUDIO_URL = "EXTRA_AUDIO_URL"
        const val NOTIFICATION_ID = 1
    }
}
