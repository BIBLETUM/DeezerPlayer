package com.example.deezerplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.deezerplayer.screen.track_list.remote_tracks.RemoteTracksScreenRoot
import com.example.deezerplayer.theme.DeezerPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeezerPlayerTheme {
                RemoteTracksScreenRoot()
            }
        }
    }
}