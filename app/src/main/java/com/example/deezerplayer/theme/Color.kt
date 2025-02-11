package com.example.deezerplayer.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PlayerColorScheme(
    val text: Color,
    val backgroundDisabled: Color,
    val placeholder: Color,
    val primaryStroke: Color,
    val primary: Color,
    val error: Color,
    val gray: Color,
)

val LightColorScheme = PlayerColorScheme(
    text = Color(0xFFF2F2F2),
    backgroundDisabled = Color(0xFFF6F6FA),
    placeholder = Color(0xFFADB5BD),
    primaryStroke = Color(0xFF9A41FE),
    error = Color(0xFFF0114C),
    primary = Color(0xFF9A10F0),
    gray = Color(0xFF76778E),
)

val LocalColorScheme = staticCompositionLocalOf {
    LightColorScheme
}