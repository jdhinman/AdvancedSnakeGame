# Advanced Snake Game - Design System

A comprehensive design system built on Material Design 3 foundations with game-specific enhancements.

## Overview

The Advanced Snake Game design system provides:
- **Consistent visual language** across all screens
- **Dark-optimized color schemes** for gaming
- **Comprehensive typography** hierarchy
- **Accessible design** patterns
- **Modern animation** language
- **Flexible component** library

## Design Principles

### 1. **Gaming-First Experience**
- Dark theme optimized for extended gameplay
- High contrast ratios for clarity
- Reduced eye strain with warm color temperatures
- Clear visual hierarchy for game information

### 2. **Accessibility & Inclusion**
- WCAG 2.1 AA compliant contrast ratios
- Touch targets minimum 48dp
- Screen reader friendly components
- Reduced motion support

### 3. **Performance & Smoothness**
- 60fps animations as standard
- Hardware-accelerated transitions
- Optimized for mobile performance
- Consistent 300ms timing for transitions

### 4. **Visual Consistency**
- Unified color palette across screens
- Consistent spacing and typography
- Predictable interaction patterns
- Cohesive iconography system

## Color System

### Primary Palette

The green nature theme emphasizes growth, achievement, and the organic movement of the snake.

```kotlin
// Light Theme
primary = Color(0xFF2E7D32)           // Forest Green
onPrimary = Color.White
primaryContainer = Color(0xFFA8F5A3)  // Light Green
onPrimaryContainer = Color(0xFF00210A) // Dark Forest

// Dark Theme  
primary = Color(0xFF4CAF50)           // Vibrant Green
onPrimary = Color(0xFF003910)
primaryContainer = Color(0xFF2E7D32)  // Deep Green
onPrimaryContainer = Color(0xFFA8F5A3) // Mint Green
```

### Secondary Palette

Supporting colors for UI elements and states.

```kotlin
// Dark Theme
secondary = Color(0xFF81C784)         // Soft Green
onSecondary = Color(0xFF263F27)
secondaryContainer = Color(0xFF1A2F1D) // Dark Olive
onSecondaryContainer = Color(0xFFD4E8D0) // Pale Green
```

### Extended Color Palette

#### Achievement Colors
- **Gold**: `#FFD700` - First place, high achievements
- **Silver**: `#C0C0C0` - Second place, good performance  
- **Bronze**: `#CD7F32` - Third place, solid effort

#### Game State Colors
- **Success**: `#4CAF50` - Positive feedback, completions
- **Warning**: `#FF9800` - Cautions, alerts
- **Error**: `#FF5449` - Failures, critical states

#### Interactive States
- **Hover**: `#66BB6A` - Element focus states
- **Pressed**: `#2E7D32` - Active press feedback
- **Disabled**: `#424242` - Inactive elements

#### Background Gradients
- **Start**: `#0D1B0F` - Deep forest
- **Mid**: `#1A2F1D` - Forest floor
- **End**: `#2E4A32` - Canopy

#### Elevation Surfaces
- **Surface1**: `#111C13` - Base elevation
- **Surface2**: `#152017` - Card elevation
- **Surface3**: `#19241B` - Modal elevation
- **Surface4**: `#1D281F` - Navigation elevation
- **Surface5**: `#212C23` - Maximum elevation

### Color Usage Guidelines

#### Contrast Requirements
- **Normal text**: 4.5:1 minimum contrast ratio
- **Large text**: 3:1 minimum contrast ratio
- **Interactive elements**: 3:1 minimum contrast ratio
- **Graphics**: 3:1 minimum contrast ratio

#### Color Accessibility
```kotlin
// High contrast examples
Text(
    text = "Score: 1250",
    color = MaterialTheme.colorScheme.onSurface, // Ensures contrast
    style = MaterialTheme.typography.extended.gameScore
)

// Status colors with sufficient contrast
Icon(
    imageVector = GameIcons.GameState.success,
    tint = MaterialTheme.extendedColors.gameSuccess, // Green with proper contrast
    contentDescription = "Game completed successfully"
)
```

## Typography System

### Font Hierarchy

Built on system fonts for consistency and performance:
- **Primary**: `FontFamily.Default` (Roboto on Android)
- **Monospace**: `FontFamily.Monospace` (Roboto Mono) for scores
- **Display**: Enhanced weights and spacing for impact

### Typography Scale

#### Display Typography
```kotlin
displayLarge: 57sp / 64sp line height / -0.25sp tracking
displayMedium: 45sp / 52sp line height / 0sp tracking  
displaySmall: 36sp / 44sp line height / 0sp tracking
```

#### Headline Typography
```kotlin
headlineLarge: 32sp / 40sp line height / 0sp tracking
headlineMedium: 28sp / 36sp line height / 0sp tracking
headlineSmall: 24sp / 32sp line height / 0sp tracking
```

#### Title Typography
```kotlin
titleLarge: 22sp / 28sp line height / 0sp tracking
titleMedium: 16sp / 24sp line height / 0.15sp tracking
titleSmall: 14sp / 20sp line height / 0.1sp tracking
```

#### Body Typography
```kotlin
bodyLarge: 16sp / 24sp line height / 0.5sp tracking
bodyMedium: 14sp / 20sp line height / 0.25sp tracking
bodySmall: 12sp / 16sp line height / 0.4sp tracking
```

#### Label Typography
```kotlin
labelLarge: 14sp / 20sp line height / 0.1sp tracking
labelMedium: 12sp / 16sp line height / 0.5sp tracking
labelSmall: 11sp / 16sp line height / 0.5sp tracking
```

### Extended Typography

#### Game-Specific Styles
```kotlin
gameTitle: FontFamily.Default / ExtraBold / 36sp / 44sp line / 2sp tracking
gameSubtitle: FontFamily.Default / Light / 20sp / 28sp line / 6sp tracking
gameScore: FontFamily.Monospace / Bold / 24sp / 32sp line / 1sp tracking
gameLevel: FontFamily.Default / SemiBold / 14sp / 20sp line / 0.5sp tracking
gameHUD: FontFamily.Default / Medium / 12sp / 16sp line / 0.25sp tracking
```

#### Settings Styles
```kotlin
settingsSection: FontFamily.Default / Bold / 18sp / 24sp line / 0.15sp tracking
settingsItem: FontFamily.Default / Medium / 16sp / 24sp line / 0.15sp tracking
settingsDescription: FontFamily.Default / Normal / 14sp / 20sp line / 0.25sp tracking
settingsValue: FontFamily.Default / SemiBold / 14sp / 20sp line / 0.25sp tracking
```

#### Achievement Styles
```kotlin
achievementTitle: FontFamily.Default / Bold / 20sp / 28sp line / 0.5sp tracking
achievementDescription: FontFamily.Default / Normal / 14sp / 20sp line / 0.25sp tracking
achievementBadge: FontFamily.Default / Black / 16sp / 24sp line / 0.5sp tracking
```

#### Leaderboard Styles
```kotlin
leaderboardRank: FontFamily.Default / Bold / 18sp / 24sp line / 0sp tracking
leaderboardPlayer: FontFamily.Default / Bold / 16sp / 24sp line / 0.15sp tracking
leaderboardScore: FontFamily.Monospace / Bold / 20sp / 28sp line / 0.5sp tracking
leaderboardDetails: FontFamily.Default / Normal / 12sp / 16sp line / 0.25sp tracking
```

### Typography Usage Guidelines

#### Readability
- **Line length**: 45-75 characters optimal
- **Line height**: 1.4-1.6x font size for body text
- **Letter spacing**: Subtle adjustments for display text
- **Font weights**: Use semibold instead of bold for better readability

#### Hierarchy
```kotlin
// Clear information hierarchy
Text(
    text = "Game Statistics", // Section header
    style = MaterialTheme.typography.extended.settingsSection
)

Text(
    text = "Total Games Played", // Item label
    style = MaterialTheme.typography.extended.settingsItem  
)

Text(
    text = "25 games completed", // Supporting detail
    style = MaterialTheme.typography.extended.settingsDescription
)
```

## Shape System

### Shape Categories

#### Corner Radius Scale
- **None**: 0dp - Sharp edges for system UI
- **Extra Small**: 4dp - Small buttons, chips
- **Small**: 8dp - Cards, input fields
- **Medium**: 12dp - Default cards, dialogs
- **Large**: 16dp - Prominent cards, sheets
- **Extra Large**: 28dp - Hero elements

#### Extended Shapes

```kotlin
// Card shapes
cardSmall: RoundedCornerShape(8.dp)
cardMedium: RoundedCornerShape(12.dp)
cardLarge: RoundedCornerShape(16.dp)
cardExtraLarge: RoundedCornerShape(24.dp)

// Button shapes  
buttonSmall: RoundedCornerShape(8.dp)
buttonMedium: RoundedCornerShape(12.dp)
buttonLarge: RoundedCornerShape(16.dp)
buttonPill: RoundedCornerShape(50%) // Fully rounded

// Specialized shapes
achievementBadge: RoundedCornerShape(50%) // Circular
gameBoard: RoundedCornerShape(12.dp)
settingsCard: RoundedCornerShape(12.dp)
leaderboardCard: RoundedCornerShape(12.dp)
```

### Shape Usage Guidelines

#### Visual Hierarchy
- **Sharp corners**: System elements, navigation
- **Small radius**: Interactive elements
- **Medium radius**: Content containers
- **Large radius**: Prominent features
- **Circular**: Badges, avatars, floating actions

#### Consistency Rules
```kotlin
// Consistent card styling
AnimatedCard(
    shape = MaterialTheme.shapes.extended.cardMedium, // Always use extended shapes
    elevation = 4.dp
) { ... }

// Button shape matching
AnimatedButton(
    shape = MaterialTheme.shapes.extended.buttonMedium,
    text = "Action"
)
```

## Spacing System

### Spatial Units

Based on 8dp grid system for consistent rhythm.

```kotlin
// Base units
val space1 = 4.dp   // 0.5x - Tight spacing
val space2 = 8.dp   // 1x   - Base unit
val space3 = 12.dp  // 1.5x - Small gaps
val space4 = 16.dp  // 2x   - Standard spacing
val space5 = 20.dp  // 2.5x - Comfortable gaps
val space6 = 24.dp  // 3x   - Section spacing
val space8 = 32.dp  // 4x   - Large sections
val space10 = 40.dp // 5x   - Screen margins
val space12 = 48.dp // 6x   - Major sections
```

### Layout Spacing

#### Component Padding
```kotlin
// Internal component spacing
Column(
    modifier = Modifier.padding(16.dp), // Standard padding
    verticalArrangement = Arrangement.spacedBy(12.dp) // Related items
)

// Card content spacing
AnimatedCard {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) { ... }
}
```

#### Screen Layout
```kotlin
// Screen-level spacing
LazyColumn(
    contentPadding = PaddingValues(16.dp), // Screen edges
    verticalArrangement = Arrangement.spacedBy(20.dp) // Major sections
)
```

#### Touch Targets
- **Minimum size**: 48dp x 48dp
- **Recommended**: 56dp x 56dp for primary actions
- **Spacing**: 8dp minimum between touch targets

## Iconography

### Icon System Principles

1. **Consistent style**: Material Design outlined icons
2. **Appropriate sizing**: 16dp, 20dp, 24dp, 32dp standard sizes
3. **Semantic meaning**: Icons reinforce text labels
4. **Cultural sensitivity**: Universal symbols preferred

### Icon Categories

#### Navigation Icons
```kotlin
GameIcons.play          // ▶️ Start game
GameIcons.pause         // ⏸️ Pause game  
GameIcons.back          // ← Return navigation
GameIcons.home          // 🏠 Main menu
GameIcons.close         // ✕ Close dialog
```

#### Settings Icons
```kotlin
GameIcons.Settings.gamepad    // 🎮 Gameplay settings
GameIcons.Settings.audio      // 🔊 Audio settings
GameIcons.Settings.visual     // 🎨 Visual settings
GameIcons.Settings.display    // 📱 Display settings
GameIcons.Settings.player     // 👤 Player settings
```

#### Game State Icons
```kotlin
GameIcons.GameState.success   // ✅ Success state
GameIcons.GameState.warning   // ⚠️ Warning state
GameIcons.GameState.error     // ❌ Error state
GameIcons.GameState.loading   // 🔄 Loading state
```

### Icon Usage Guidelines

#### Sizing Standards
```kotlin
// Navigation and primary actions
Icon(
    imageVector = GameIcons.play,
    modifier = Modifier.size(24.dp),
    contentDescription = "Start game"
)

// Secondary actions and indicators  
Icon(
    imageVector = GameIcons.Settings.audio,
    modifier = Modifier.size(20.dp),
    contentDescription = "Audio settings"
)

// Small indicators and status
Icon(
    imageVector = GameIcons.Status.active,
    modifier = Modifier.size(16.dp),
    contentDescription = "Active status"
)
```

#### Color Application
```kotlin
// Primary actions
Icon(
    tint = MaterialTheme.colorScheme.primary,
    imageVector = GameIcons.play
)

// Interactive states
Icon(
    tint = if (isEnabled) 
        MaterialTheme.colorScheme.primary 
    else 
        MaterialTheme.colorScheme.onSurfaceVariant,
    imageVector = GameIcons.Settings.audio
)

// Status indication
Icon(
    tint = when (state) {
        "success" -> MaterialTheme.extendedColors.gameSuccess
        "warning" -> MaterialTheme.extendedColors.gameWarning
        "error" -> MaterialTheme.extendedColors.gameError
        else -> MaterialTheme.colorScheme.onSurface
    },
    imageVector = getGameStateIcon(state)
)
```

## Motion & Animation

### Animation Principles

1. **Purposeful**: Every animation serves a functional purpose
2. **Responsive**: Immediate feedback for user actions
3. **Natural**: Physics-based easing and timing
4. **Subtle**: Enhancement, not distraction
5. **Accessible**: Respects motion preferences

### Timing Standards

```kotlin
// Interaction feedback
val quickTiming = 150.millis     // Button press, switch toggle
val standardTiming = 300.millis  // Screen transitions, card expand
val complexTiming = 600.millis   // Screen enter, complex layouts
val progressTiming = 1000.millis // Progress bars, counters
```

### Easing Functions

```kotlin
// Standard Material Design easing
FastOutSlowInEasing    // Standard transitions (enter/exit)
LinearOutSlowInEasing  // Enter animations
FastOutLinearInEasing  // Exit animations
LinearEasing           // Infinite animations, progress
```

### Animation Patterns

#### Screen Transitions
```kotlin
AnimatedVisibility(
    visible = isVisible,
    enter = fadeIn(animationSpec = tween(600)) + slideInFromBottom(),
    exit = fadeOut(animationSpec = tween(300)) + slideOutToBottom()
)
```

#### Interactive Feedback
```kotlin
// Button press animation
val scale = remember { Animatable(1f) }

LaunchedEffect(interactionSource) {
    interactionSource.interactions.collect { interaction ->
        when (interaction) {
            is PressInteraction.Press -> scale.animateTo(0.95f, tween(150))
            is PressInteraction.Release -> scale.animateTo(1f, tween(150))
        }
    }
}
```

#### Content Reveal
```kotlin
// Staggered entry animations
itemsIndexed(items) { index, item ->
    EnhancedLeaderboardEntry(
        score = item,
        showEntryAnimation = true,
        animationDelay = index * 100 // Stagger by 100ms
    )
}
```

### Performance Guidelines

1. **Limit concurrent animations**: Maximum 3-4 simultaneous
2. **Use hardware acceleration**: Prefer `scale`, `alpha`, `rotation`
3. **Avoid layout animations**: In frequently updated content
4. **Cache animation instances**: Use `remember` for Animatable
5. **Respect reduced motion**: Provide alternatives

## Layout Principles

### Grid System

8dp base grid system for consistent spatial relationships.

#### Screen Structure
```
┌─ 16dp margin ─┐
│ ┌─ Content ─┐ │ 16dp margin
│ │           │ │
│ │  20dp     │ │ (between major sections)
│ │  spacing  │ │
│ │           │ │
│ └───────────┘ │
└─ 16dp margin ─┘
```

#### Component Hierarchy
1. **Screen level**: 16dp margins, 20dp section spacing
2. **Card level**: 16dp internal padding, 12dp item spacing  
3. **Row level**: 8-12dp horizontal spacing between elements
4. **Inline level**: 4-8dp spacing between related elements

### Responsive Design

#### Breakpoints
- **Compact**: 0-599dp width (phones)
- **Medium**: 600-839dp width (tablets, foldables)
- **Expanded**: 840dp+ width (tablets, desktop)

#### Adaptive Layouts
```kotlin
// Responsive spacing
val horizontalPadding = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
    16.dp
} else {
    32.dp
}

// Adaptive columns
LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 320.dp),
    contentPadding = PaddingValues(horizontalPadding)
)
```

## Component Architecture

### Design Principles

1. **Composition over inheritance**: Prefer composable functions
2. **Single responsibility**: Each component has one clear purpose
3. **Consistent API**: Similar parameters across similar components
4. **Extensible design**: Easy to customize and extend
5. **Performance optimized**: Efficient recomposition

### Component Hierarchy

```
Theme System
├── ExtendedColors
├── ExtendedTypography  
├── ExtendedShapes
└── Animation Standards

Common Components
├── AnimatedCard
├── AnimatedButton
├── AnimatedIconButton
├── AnimatedSwitch
├── AchievementBadge
├── AnimatedCounter
└── PulsingIndicator

Settings Components
├── EnhancedSettingsSection
├── EnhancedSwitchSetting
├── EnhancedSliderSetting
├── EnhancedRadioGroupSetting
├── EnhancedTextFieldSetting
└── SettingsPreview

Leaderboard Components
├── EnhancedRankIndicator
├── EnhancedLeaderboardEntry
├── EnhancedStatisticsCard
├── AchievementProgress
└── EnhancedEmptyLeaderboard
```

### API Consistency

#### Common Parameters
```kotlin
// Standard across all animated components
showAnimation: Boolean = false
animationDuration: Int = 300
animationDelay: Int = 0

// Standard across all enhanced components  
title: String
description: String? = null
icon: ImageVector? = null
enabled: Boolean = true

// Standard styling parameters
modifier: Modifier = Modifier
backgroundColor: Color = MaterialTheme.colorScheme.surface
contentColor: Color = MaterialTheme.colorScheme.onSurface
```

## Implementation Guidelines

### Code Organization

```
presentation/theme/
├── Theme.kt              // Main theme and extended colors
├── Typography.kt         // Typography system and extensions
├── Shapes.kt            // Shape system and extensions  
├── IconSystem.kt        // Comprehensive icon definitions
├── CommonComponents.kt  // Reusable base components
├── SettingsComponents.kt // Settings-specific components
└── LeaderboardComponents.kt // Leaderboard-specific components
```

### Usage Patterns

#### Theme Application
```kotlin
// Always wrap app content in theme
AdvancedSnakeGameTheme {
    // App content with access to extended theme
    AnimatedCard(
        backgroundColor = MaterialTheme.extendedColors.elevationSurface2
    ) { ... }
}
```

#### Component Composition
```kotlin
// Compose complex UIs from simple components
EnhancedSettingsSection(
    title = "Audio",
    icon = GameIcons.Settings.audio
) {
    EnhancedSwitchSetting(...)
    EnhancedSliderSetting(...)
    SettingsPreview(...)
}
```

#### Animation Coordination
```kotlin
// Coordinate multiple animations
LaunchedEffect(showScreen) {
    delay(300) // Initial delay
    isVisible = true // Trigger content animations
}

AnimatedVisibility(visible = isVisible) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            AnimatedComponent(
                showAnimation = true,
                animationDelay = index * 100 // Staggered reveal
            )
        }
    }
}
```

## Quality Assurance

### Testing Requirements

1. **Visual consistency**: Compare components across screens
2. **Animation performance**: Profile on target devices
3. **Accessibility compliance**: Test with screen readers
4. **Responsive behavior**: Test across screen sizes
5. **Dark theme support**: Verify all screens and states

### Design Review Checklist

- [ ] Consistent use of extended color palette
- [ ] Proper typography hierarchy applied
- [ ] Appropriate shape usage throughout
- [ ] Consistent spacing and alignment
- [ ] Meaningful animations with proper timing
- [ ] Accessible contrast ratios maintained
- [ ] Touch targets meet minimum requirements
- [ ] Icons support component meaning
- [ ] Responsive behavior on different screens
- [ ] Performance optimized for target devices

This design system provides the foundation for a cohesive, accessible, and engaging user experience across the Advanced Snake Game application.