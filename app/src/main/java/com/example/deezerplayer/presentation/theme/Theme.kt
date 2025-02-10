package com.example.deezerplayer.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun DeezerPlayerTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColorScheme provides LightColorScheme,
    ) {
        MaterialTheme(
            typography = Typography,
            content = content
        )
    }

}

object PlayerTheme {
    val colors: PlayerColorScheme
        @Composable get() = LocalColorScheme.current
}