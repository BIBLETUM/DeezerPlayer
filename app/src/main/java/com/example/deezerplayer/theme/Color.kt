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
    val neutralActive: Color,
    val gradientViolet: List<Pair<Float, Color>>,
    val buttonTextDisabled: Color,
    val error: Color,
    val neutralWhite: Color,
    val gray: Color,
)

val LightColorScheme = PlayerColorScheme(
    text = Color(0xFFF2F2F2),
    backgroundDisabled = Color(0xFFF6F6FA),
    placeholder = Color(0xFFADB5BD),
    primaryStroke = Color(0xFF9A41FE),
    neutralActive = Color(0xFF29183B),
    error = Color(0xFFF0114C),
    neutralWhite = Color(0xFFFFFFFF),
    buttonTextDisabled = Color(0xFF9797AF),
    primary = Color(0xFF9A10F0),
    gray = Color(0xFF76778E),
    gradientViolet = listOf(
        0.0f to Color(0xFFED3CCA),
        0.15f to Color(0xFFDF34D2),
        0.29f to Color(0xFFD02BD9),
        0.43f to Color(0xFFBF22E1),
        0.57f to Color(0xFFAE1AE8),
        0.71f to Color(0xFF9A10F0),
        0.85f to Color(0xFF8306F7),
        1f to Color(0xFF6600FF)
    ),
)

val LocalColorScheme = staticCompositionLocalOf {
    LightColorScheme
}