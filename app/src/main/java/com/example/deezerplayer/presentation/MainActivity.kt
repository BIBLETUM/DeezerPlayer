package com.example.deezerplayer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.deezerplayer.presentation.screen.remote_tracks.RemoteTracksScreenRoot
import com.example.deezerplayer.presentation.theme.DeezerPlayerTheme

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