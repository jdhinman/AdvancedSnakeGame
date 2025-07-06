package com.advancedsnake.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class ExtendedShapes(
    // Card shapes
    val cardSmall: RoundedCornerShape,
    val cardMedium: RoundedCornerShape,
    val cardLarge: RoundedCornerShape,
    val cardExtraLarge: RoundedCornerShape,
    
    // Button shapes
    val buttonSmall: RoundedCornerShape,
    val buttonMedium: RoundedCornerShape,
    val buttonLarge: RoundedCornerShape,
    val buttonPill: RoundedCornerShape,
    
    // Input shapes
    val inputField: RoundedCornerShape,
    val inputFieldFocused: RoundedCornerShape,
    
    // Dialog shapes
    val dialogSmall: RoundedCornerShape,
    val dialogMedium: RoundedCornerShape,
    val dialogLarge: RoundedCornerShape,
    
    // Achievement shapes
    val achievementBadge: RoundedCornerShape,
    val achievementCard: RoundedCornerShape,
    
    // Special shapes
    val gameBoard: RoundedCornerShape,
    val gameHUD: RoundedCornerShape,
    val settingsCard: RoundedCornerShape,
    val leaderboardCard: RoundedCornerShape
)

private val gameExtendedShapes = ExtendedShapes(
    // Card shapes
    cardSmall = RoundedCornerShape(8.dp),
    cardMedium = RoundedCornerShape(12.dp),
    cardLarge = RoundedCornerShape(16.dp),
    cardExtraLarge = RoundedCornerShape(24.dp),
    
    // Button shapes
    buttonSmall = RoundedCornerShape(8.dp),
    buttonMedium = RoundedCornerShape(12.dp),
    buttonLarge = RoundedCornerShape(16.dp),
    buttonPill = RoundedCornerShape(50),
    
    // Input shapes
    inputField = RoundedCornerShape(8.dp),
    inputFieldFocused = RoundedCornerShape(12.dp),
    
    // Dialog shapes
    dialogSmall = RoundedCornerShape(16.dp),
    dialogMedium = RoundedCornerShape(20.dp),
    dialogLarge = RoundedCornerShape(24.dp),
    
    // Achievement shapes
    achievementBadge = RoundedCornerShape(50),
    achievementCard = RoundedCornerShape(16.dp),
    
    // Special shapes
    gameBoard = RoundedCornerShape(12.dp),
    gameHUD = RoundedCornerShape(8.dp),
    settingsCard = RoundedCornerShape(12.dp),
    leaderboardCard = RoundedCornerShape(12.dp)
)

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

val LocalExtendedShapes = staticCompositionLocalOf { gameExtendedShapes }

// Extension property to access extended shapes
@get:Composable
val Shapes.extended: ExtendedShapes
    get() = LocalExtendedShapes.current