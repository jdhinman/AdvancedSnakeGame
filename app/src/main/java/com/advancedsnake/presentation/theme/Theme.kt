package com.advancedsnake.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA8F5A3),
    onPrimaryContainer = Color(0xFF00210A),
    secondary = Color(0xFF526350),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD4E8D0),
    onSecondaryContainer = Color(0xFF101F10),
    tertiary = Color(0xFF39656B),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFBCEBF2),
    onTertiaryContainer = Color(0xFF001F23),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF8FDF4),
    onBackground = Color(0xFF191D16),
    surface = Color(0xFFF8FDF4),
    onSurface = Color(0xFF191D16),
    surfaceVariant = Color(0xFFDDE5DA),
    onSurfaceVariant = Color(0xFF414941),
    outline = Color(0xFF717971)
)

@Composable
fun AdvancedSnakeGameTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}