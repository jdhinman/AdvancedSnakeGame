package com.advancedsnake.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Extended color palette for game-specific colors
@Immutable
data class ExtendedColors(
    // Achievement colors
    val achievementGold: Color,
    val achievementSilver: Color,
    val achievementBronze: Color,
    
    // Game state colors
    val gameSuccess: Color,
    val gameWarning: Color,
    val gameError: Color,
    
    // Interactive state colors
    val interactiveHover: Color,
    val interactivePressed: Color,
    val interactiveDisabled: Color,
    
    // Gradient colors
    val gradientStart: Color,
    val gradientMid: Color,
    val gradientEnd: Color,
    
    // Status colors
    val statusActive: Color,
    val statusInactive: Color,
    val statusPending: Color,
    
    // Elevation colors
    val elevationSurface1: Color,
    val elevationSurface2: Color,
    val elevationSurface3: Color,
    val elevationSurface4: Color,
    val elevationSurface5: Color
)

private val LightExtendedColors = ExtendedColors(
    achievementGold = Color(0xFFFFD700),
    achievementSilver = Color(0xFFC0C0C0),
    achievementBronze = Color(0xFFCD7F32),
    
    gameSuccess = Color(0xFF4CAF50),
    gameWarning = Color(0xFFFF9800),
    gameError = Color(0xFFF44336),
    
    interactiveHover = Color(0xFF388E3C),
    interactivePressed = Color(0xFF1B5E20),
    interactiveDisabled = Color(0xFFBDBDBD),
    
    gradientStart = Color(0xFF2E7D32),
    gradientMid = Color(0xFF4CAF50),
    gradientEnd = Color(0xFF81C784),
    
    statusActive = Color(0xFF4CAF50),
    statusInactive = Color(0xFF9E9E9E),
    statusPending = Color(0xFFFF9800),
    
    elevationSurface1 = Color(0xFFF3F7F0),
    elevationSurface2 = Color(0xFFEEF4EA),
    elevationSurface3 = Color(0xFFE8F0E4),
    elevationSurface4 = Color(0xFFE3EDDE),
    elevationSurface5 = Color(0xFFDEE9D8)
)

private val DarkExtendedColors = ExtendedColors(
    achievementGold = Color(0xFFFFD700),
    achievementSilver = Color(0xFFC0C0C0),
    achievementBronze = Color(0xFFCD7F32),
    
    gameSuccess = Color(0xFF4CAF50),
    gameWarning = Color(0xFFFF9800),
    gameError = Color(0xFFFF5449),
    
    interactiveHover = Color(0xFF66BB6A),
    interactivePressed = Color(0xFF2E7D32),
    interactiveDisabled = Color(0xFF424242),
    
    gradientStart = Color(0xFF0D1B0F),
    gradientMid = Color(0xFF1A2F1D),
    gradientEnd = Color(0xFF2E4A32),
    
    statusActive = Color(0xFF4CAF50),
    statusInactive = Color(0xFF616161),
    statusPending = Color(0xFFFF9800),
    
    elevationSurface1 = Color(0xFF111C13),
    elevationSurface2 = Color(0xFF152017),
    elevationSurface3 = Color(0xFF19241B),
    elevationSurface4 = Color(0xFF1D281F),
    elevationSurface5 = Color(0xFF212C23)
)

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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color(0xFF003910),
    primaryContainer = Color(0xFF2E7D32),
    onPrimaryContainer = Color(0xFFA8F5A3),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF263F27),
    secondaryContainer = Color(0xFF1A2F1D),
    onSecondaryContainer = Color(0xFFD4E8D0),
    tertiary = Color(0xFF4DB6AC),
    onTertiary = Color(0xFF00363A),
    tertiaryContainer = Color(0xFF2E4A32),
    onTertiaryContainer = Color(0xFFBCEBF2),
    error = Color(0xFFFF5449),
    onError = Color(0xFF410002),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0D1B0F),
    onBackground = Color(0xFFE0E3DC),
    surface = Color(0xFF0D1B0F),
    onSurface = Color(0xFFE0E3DC),
    surfaceVariant = Color(0xFF1A2F1D),
    onSurfaceVariant = Color(0xFFBFC9BD),
    outline = Color(0xFF8A938A)
)

val LocalExtendedColors = staticCompositionLocalOf { DarkExtendedColors }

@Composable
fun AdvancedSnakeGameTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors
    
    androidx.compose.runtime.CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides gameExtendedTypography,
        LocalExtendedShapes provides gameExtendedShapes
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

// Extension property to access extended colors
val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    get() = LocalExtendedColors.current