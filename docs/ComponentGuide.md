# Advanced Snake Game - Component Guide

This guide provides comprehensive documentation for using the enhanced design system components in the Advanced Snake Game.

## Overview

The component library provides:
- **Consistent styling** across all screens
- **Modern animations** and interactions
- **Accessibility-first design** principles  
- **Dark mode optimized** components
- **Modular and reusable** architecture

## Core Theme System

### Extended Colors

Access extended colors through `MaterialTheme.extendedColors`:

```kotlin
// Achievement colors
MaterialTheme.extendedColors.achievementGold
MaterialTheme.extendedColors.achievementSilver
MaterialTheme.extendedColors.achievementBronze

// Game state colors
MaterialTheme.extendedColors.gameSuccess
MaterialTheme.extendedColors.gameWarning
MaterialTheme.extendedColors.gameError

// Interactive states
MaterialTheme.extendedColors.interactiveHover
MaterialTheme.extendedColors.interactivePressed
MaterialTheme.extendedColors.interactiveDisabled

// Gradient backgrounds
MaterialTheme.extendedColors.gradientStart
MaterialTheme.extendedColors.gradientMid
MaterialTheme.extendedColors.gradientEnd

// Elevation surfaces
MaterialTheme.extendedColors.elevationSurface1
MaterialTheme.extendedColors.elevationSurface2
MaterialTheme.extendedColors.elevationSurface3
```

### Extended Typography

Access extended typography through `MaterialTheme.typography.extended`:

```kotlin
// Game-specific typography
MaterialTheme.typography.extended.gameTitle
MaterialTheme.typography.extended.gameScore
MaterialTheme.typography.extended.gameLevel

// Settings typography
MaterialTheme.typography.extended.settingsSection
MaterialTheme.typography.extended.settingsItem
MaterialTheme.typography.extended.settingsDescription

// Achievement typography
MaterialTheme.typography.extended.achievementTitle
MaterialTheme.typography.extended.achievementBadge

// Button typography
MaterialTheme.typography.extended.buttonPrimary
MaterialTheme.typography.extended.buttonSecondary
```

### Extended Shapes

Access extended shapes through `MaterialTheme.shapes.extended`:

```kotlin
// Card shapes
MaterialTheme.shapes.extended.cardSmall
MaterialTheme.shapes.extended.cardMedium
MaterialTheme.shapes.extended.cardLarge

// Button shapes
MaterialTheme.shapes.extended.buttonMedium
MaterialTheme.shapes.extended.buttonPill

// Specialized shapes
MaterialTheme.shapes.extended.achievementBadge
MaterialTheme.shapes.extended.settingsCard
MaterialTheme.shapes.extended.leaderboardCard
```

## Common Components

### AnimatedCard

Enhanced card component with hover effects and animations.

```kotlin
AnimatedCard(
    onClick = { /* Handle click */ },
    elevation = 8.dp,
    shape = MaterialTheme.shapes.extended.cardMedium,
    backgroundColor = MaterialTheme.extendedColors.elevationSurface2,
    border = true,
    borderColor = MaterialTheme.colorScheme.primary
) {
    Text("Card content")
}
```

**Parameters:**
- `onClick`: Optional click handler
- `enabled`: Enable/disable interactions
- `elevation`: Card elevation (default: 4.dp)
- `shape`: Card shape (default: cardMedium)
- `backgroundColor`: Background color
- `border`: Show border outline
- `animationDuration`: Animation timing (default: 200ms)

### AnimatedButton

Modern button with consistent styling and press animations.

```kotlin
AnimatedButton(
    onClick = { /* Handle click */ },
    text = "Primary Action",
    icon = GameIcons.play,
    isPrimary = true,
    iconPosition = ButtonIconPosition.START
)
```

**Parameters:**
- `text`: Button text
- `icon`: Optional icon
- `isPrimary`: Primary or secondary styling
- `iconPosition`: START or END icon placement
- `enabled`: Enable/disable button
- `shape`: Button shape
- `animationDuration`: Press animation timing

### AnimatedIconButton

Icon button with hover and press animations.

```kotlin
AnimatedIconButton(
    onClick = { /* Handle click */ },
    icon = GameIcons.Settings.audio,
    contentDescription = "Toggle audio",
    tint = MaterialTheme.colorScheme.primary,
    size = 24.dp
)
```

### AnimatedSwitch

Enhanced switch with scale animations on state changes.

```kotlin
AnimatedSwitch(
    checked = isEnabled,
    onCheckedChange = { isEnabled = it },
    colors = SwitchDefaults.colors(
        checkedThumbColor = MaterialTheme.colorScheme.primary
    )
)
```

### AchievementBadge

Animated badge for achievements and ranks.

```kotlin
AchievementBadge(
    rank = 1, // 1=gold, 2=silver, 3=bronze
    size = 32.dp,
    isUnlocked = true,
    showUnlockAnimation = true
)
```

### AnimatedCounter

Counting animation for scores and statistics.

```kotlin
AnimatedCounter(
    targetValue = 1250,
    textStyle = MaterialTheme.typography.headlineSmall,
    color = MaterialTheme.colorScheme.primary,
    animationDuration = 1000,
    prefix = "Score: ",
    suffix = " pts"
)
```

### PulsingIndicator

Loading indicator with pulsing animation.

```kotlin
PulsingIndicator(
    color = MaterialTheme.colorScheme.primary,
    size = 24.dp,
    animationDuration = 1000
)
```

## Settings Components

### EnhancedSettingsSection

Container for settings groups with collapsible sections.

```kotlin
EnhancedSettingsSection(
    title = "Audio & Feedback",
    icon = GameIcons.Settings.audio,
    subtitle = "Sound and vibration options",
    isExpanded = true,
    onExpandChange = { expanded -> /* Handle expansion */ }
) {
    // Settings content
    EnhancedSwitchSetting(...)
    EnhancedSliderSetting(...)
}
```

### EnhancedSwitchSetting

Switch setting with icon and description.

```kotlin
EnhancedSwitchSetting(
    title = "Sound Effects",
    description = "Play audio feedback during gameplay",
    checked = settings.soundEffectsEnabled,
    onCheckedChange = { /* Update setting */ },
    icon = GameIcons.Settings.audio,
    enabled = true
)
```

### EnhancedSliderSetting

Slider setting with value display and labels.

```kotlin
EnhancedSliderSetting(
    title = "Control Sensitivity",
    value = settings.controlSensitivity,
    onValueChange = { /* Update setting */ },
    valueRange = 0.5f..2.0f,
    steps = 5,
    icon = GameIcons.Snake.direction,
    description = "Swipe gesture sensitivity",
    minLabel = "Low",
    maxLabel = "High",
    valueFormatter = { value ->
        when {
            value <= 0.8f -> "Low"
            value <= 1.2f -> "Normal"
            else -> "High"
        }
    }
)
```

### EnhancedRadioGroupSetting

Radio button group with enhanced styling.

```kotlin
EnhancedRadioGroupSetting(
    title = "Game Speed",
    description = "Controls snake movement speed",
    icon = GameIcons.Snake.speed,
    options = GameSpeed.values().toList(),
    selectedOption = settings.gameSpeed,
    onOptionSelected = { /* Update setting */ },
    optionDisplay = { it.displayName },
    optionDescription = { "Speed: ${it.baseSpeedMs}ms" }
)
```

### EnhancedTextFieldSetting

Text input with validation and character limits.

```kotlin
EnhancedTextFieldSetting(
    title = "Player Name",
    value = settings.playerName,
    onValueChange = { /* Update setting */ },
    icon = GameIcons.Settings.player,
    description = "Your display name",
    placeholder = "Enter name",
    maxLength = 10,
    supportingText = "Used in leaderboard"
)
```

### SettingsPreview

Live preview component for setting changes.

```kotlin
SettingsPreview(
    title = "Audio Preview",
    backgroundColor = MaterialTheme.extendedColors.elevationSurface3
) {
    Row {
        Icon(...)
        Text("Audio Enabled")
    }
}
```

## Leaderboard Components

### EnhancedRankIndicator

Metallic rank badges for leaderboard positions.

```kotlin
EnhancedRankIndicator(
    rank = 1,
    size = 48.dp,
    isTopThree = true,
    showAnimation = true
)
```

### EnhancedLeaderboardEntry

Complete leaderboard entry with animations.

```kotlin
EnhancedLeaderboardEntry(
    rank = 1,
    playerName = "Player1",
    score = 1250,
    gameDetails = "Snake length: 15 • Fast speed",
    timeDetails = "2 hours ago • 3:42",
    isCurrentPlayer = false,
    isTopThree = true,
    showEntryAnimation = true,
    animationDelay = 100
)
```

### EnhancedStatisticsCard

Statistics display with visual charts.

```kotlin
EnhancedStatisticsCard(
    totalGames = 25,
    averageScore = 487.5,
    highestScore = 1250,
    winRate = 0.72f,
    longestGame = "5:23",
    showAnimations = true
)
```

### AchievementProgress

Progress indicator for achievements.

```kotlin
AchievementProgress(
    title = "High Achiever",
    description = "Reach a score of 100",
    currentProgress = 75,
    maxProgress = 100,
    icon = GameIcons.Achievement.gold,
    isUnlocked = false,
    showAnimation = true
)
```

### EnhancedEmptyLeaderboard

Engaging empty state for leaderboards.

```kotlin
EnhancedEmptyLeaderboard(
    showAnimation = true
)
```

## Icon System

### GameIcons Structure

```kotlin
// Navigation icons
GameIcons.play
GameIcons.pause
GameIcons.back
GameIcons.home

// Settings category icons
GameIcons.Settings.gamepad     // 🎮 Gameplay
GameIcons.Settings.audio       // 🔊 Audio
GameIcons.Settings.visual      // 🎨 Visual
GameIcons.Settings.display     // 📱 Display
GameIcons.Settings.player      // 👤 Player

// Achievement icons
GameIcons.Achievement.gold
GameIcons.Achievement.silver
GameIcons.Achievement.bronze
GameIcons.Achievement.unlock
GameIcons.Achievement.progress

// Game state icons
GameIcons.GameState.success
GameIcons.GameState.warning
GameIcons.GameState.error
GameIcons.GameState.loading

// Snake game specific
GameIcons.Snake.snake
GameIcons.Snake.food
GameIcons.Snake.grid
GameIcons.Snake.speed
GameIcons.Snake.theme
```

### Helper Functions

```kotlin
// Get achievement icon by rank
val icon = getAchievementIcon(rank = 1) // Returns gold icon

// Get rank icon by position  
val rankIcon = getRankIcon(rank = 1) // Returns rank1 icon

// Get status icon by state
val statusIcon = getStatusIcon(isActive = true)

// Get game state icon
val gameIcon = getGameStateIcon("success")

// Get audio icon by state
val audioIcon = getAudioIcon(isEnabled = true)
```

## Animation Guidelines

### Timing Standards

- **Quick interactions**: 150ms (button presses)
- **Standard transitions**: 300ms (screen changes)
- **Complex animations**: 600ms (entrance animations)
- **Loading states**: 1000ms (progress animations)

### Easing Functions

- **FastOutSlowInEasing**: Standard transitions
- **LinearEasing**: Infinite animations
- **FastOutLinearInEasing**: Exit animations
- **LinearOutSlowInEasing**: Enter animations

### Animation Best Practices

1. **Use consistent timing** across similar interactions
2. **Provide visual feedback** for all user actions
3. **Respect accessibility** settings for reduced motion
4. **Layer animations** with appropriate delays
5. **Test performance** on lower-end devices

## Accessibility

### Built-in Features

- **High contrast** color schemes
- **Semantic descriptions** for screen readers
- **Touch target sizing** (minimum 48dp)
- **Focus indicators** for keyboard navigation
- **Motion reduction** support

### Implementation

```kotlin
// Semantic descriptions
Icon(
    imageVector = GameIcons.Settings.audio,
    contentDescription = "Toggle sound effects"
)

// Touch target sizing
IconButton(
    modifier = Modifier.size(48.dp), // Minimum touch target
    onClick = { /* Action */ }
) {
    Icon(modifier = Modifier.size(24.dp), ...)
}

// Focus indicators
Button(
    modifier = Modifier.focusable(),
    onClick = { /* Action */ }
) { ... }
```

## Performance Considerations

### Animation Performance

- Use `Animatable` for simple value animations
- Use `AnimatedVisibility` for layout animations
- Avoid animating layout properties in loops
- Use `remember` for animation instances

### Memory Optimization

- Use `staticCompositionLocalOf` for theme values
- Implement proper `@Stable` and `@Immutable` annotations
- Avoid creating new objects in composition

### Best Practices

1. **Profile animations** on target devices
2. **Use hardware acceleration** when available
3. **Limit simultaneous animations** (max 3-4)
4. **Cache animation instances** with remember
5. **Dispose resources** properly in effects

## Migration Guide

### From Old Components

```kotlin
// Old approach
Card(
    elevation = CardDefaults.cardElevation(4.dp)
) {
    // Content
}

// New approach  
AnimatedCard(
    elevation = 4.dp,
    shape = MaterialTheme.shapes.extended.cardMedium
) {
    // Content
}
```

### From Standard Material3

```kotlin
// Old approach
Switch(
    checked = isEnabled,
    onCheckedChange = { isEnabled = it }
)

// New approach
EnhancedSwitchSetting(
    title = "Feature Name",
    description = "Feature description",
    checked = isEnabled,
    onCheckedChange = { isEnabled = it },
    icon = GameIcons.Settings.general
)
```

## Troubleshooting

### Common Issues

1. **Animation not playing**: Check `showAnimation` parameter
2. **Colors not updating**: Ensure using `MaterialTheme.extendedColors`
3. **Icons not displaying**: Verify import of `GameIcons`
4. **Layout jumping**: Use `animateContentSize()` modifier
5. **Performance issues**: Reduce concurrent animations

### Debug Tips

- Use Layout Inspector to verify component structure
- Enable animation scale in Developer Options
- Test with different screen sizes and orientations
- Verify accessibility with TalkBack enabled

## Examples

See the enhanced `SettingsScreen.kt` and `LeaderboardScreen.kt` for complete implementation examples of all components in action.