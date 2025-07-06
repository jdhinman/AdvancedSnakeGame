package com.advancedsnake.presentation.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.advancedsnake.R
import com.advancedsnake.domain.entities.*
import com.advancedsnake.presentation.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.extendedColors.gradientStart,
                        MaterialTheme.extendedColors.gradientMid,
                        MaterialTheme.extendedColors.gradientEnd
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Enhanced Top App Bar
            TopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.extended.settingsSection,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    AnimatedIconButton(
                        onClick = { navController.popBackStack() },
                        icon = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                },
                actions = {
                    AnimatedIconButton(
                        onClick = viewModel::showResetDialog,
                        icon = GameIcons.Settings.reset,
                        contentDescription = "Reset to defaults"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PulsingIndicator(
                            size = 48.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Loading Settings...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInFromBottom()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Player Settings
                        EnhancedPlayerSettingsSection(
                            settings = uiState.settings,
                            onSettingsChange = viewModel::updateSettings
                        )

                        // Gameplay Settings
                        EnhancedGameplaySettingsSection(
                            settings = uiState.settings,
                            onSettingsChange = viewModel::updateSettings
                        )

                        // Audio & Feedback Settings
                        EnhancedAudioSettingsSection(
                            settings = uiState.settings,
                            onSettingsChange = viewModel::updateSettings
                        )

                        // Visual Settings
                        EnhancedVisualSettingsSection(
                            settings = uiState.settings,
                            onSettingsChange = viewModel::updateSettings
                        )

                        // Display Settings
                        EnhancedDisplaySettingsSection(
                            settings = uiState.settings,
                            onSettingsChange = viewModel::updateSettings
                        )
                        
                        // Add some bottom padding for scroll
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }

    // Enhanced Reset confirmation dialog
    if (uiState.showResetDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideResetDialog,
            title = { 
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = GameIcons.Settings.reset,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        "Reset Settings",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            },
            text = { 
                Text(
                    "Reset all settings to their default values? This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                AnimatedButton(
                    onClick = viewModel::resetToDefaults,
                    text = "Reset",
                    icon = GameIcons.Settings.reset
                )
            },
            dismissButton = {
                AnimatedButton(
                    onClick = viewModel::hideResetDialog,
                    text = "Cancel",
                    icon = GameIcons.UI.cancel
                )
            },
            shape = MaterialTheme.shapes.extended.dialogMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedPlayerSettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    EnhancedSettingsSection(
        title = "Player",
        icon = GameIcons.Settings.player,
        subtitle = "Customize your player profile"
    ) {
        EnhancedTextFieldSetting(
            title = "Player Name",
            value = settings.playerName,
            onValueChange = { newName ->
                onSettingsChange(settings.copy(playerName = newName.take(10)))
            },
            icon = GameIcons.Settings.player,
            description = "Your name displayed on the leaderboard",
            placeholder = "Enter your name",
            maxLength = 10,
            supportingText = "Used in leaderboard rankings"
        )
    }
}

@Composable
private fun EnhancedGameplaySettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    EnhancedSettingsSection(
        title = "Gameplay",
        icon = GameIcons.Settings.gamepad,
        subtitle = "Configure game mechanics and controls"
    ) {
        // Game Speed
        EnhancedRadioGroupSetting(
            title = "Game Speed",
            description = "Controls how fast the snake moves",
            icon = GameIcons.Snake.speed,
            options = GameSpeed.values().toList(),
            selectedOption = settings.gameSpeed,
            onOptionSelected = { onSettingsChange(settings.copy(gameSpeed = it)) },
            optionDisplay = { it.displayName },
            optionDescription = { "Start: ${it.baseSpeedMs}ms, Min: ${it.minSpeedMs}ms" }
        )

        // Board Size
        EnhancedRadioGroupSetting(
            title = "Board Size",
            description = "Dimensions of the game playing field",
            icon = GameIcons.Snake.board,
            options = BoardSize.values().toList(),
            selectedOption = settings.boardSize,
            onOptionSelected = { onSettingsChange(settings.copy(boardSize = it)) },
            optionDisplay = { "${it.displayName} (${it.width}×${it.height})" },
            optionDescription = { 
                when (it) {
                    BoardSize.SMALL -> "Perfect for quick games"
                    BoardSize.MEDIUM -> "Balanced gameplay experience"
                    BoardSize.LARGE -> "Extended gameplay sessions"
                }
            }
        )

        // Control Sensitivity
        EnhancedSliderSetting(
            title = "Control Sensitivity",
            value = settings.controlSensitivity,
            onValueChange = { onSettingsChange(settings.copy(controlSensitivity = it)) },
            valueRange = 0.5f..2.0f,
            steps = 5,
            icon = GameIcons.Snake.direction,
            description = "How sensitive swipe gestures are",
            minLabel = "Low",
            maxLabel = "High",
            valueFormatter = { 
                when {
                    it <= 0.8f -> "Low"
                    it <= 1.2f -> "Normal" 
                    it <= 1.6f -> "High"
                    else -> "Very High"
                }
            }
        )
    }
}

@Composable
private fun EnhancedAudioSettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    EnhancedSettingsSection(
        title = "Audio & Feedback",
        icon = GameIcons.Settings.audio,
        subtitle = "Sound effects and haptic feedback options"
    ) {
        EnhancedSwitchSetting(
            title = "Sound Effects",
            description = "Play audio feedback during gameplay events",
            checked = settings.soundEffectsEnabled,
            onCheckedChange = { onSettingsChange(settings.copy(soundEffectsEnabled = it)) },
            icon = getAudioIcon(settings.soundEffectsEnabled)
        )

        EnhancedSwitchSetting(
            title = "Vibration",
            description = "Haptic feedback for collisions and food consumption",
            checked = settings.vibrationEnabled,
            onCheckedChange = { onSettingsChange(settings.copy(vibrationEnabled = it)) },
            icon = GameIcons.Settings.vibration
        )
        
        // Audio preview
        SettingsPreview(
            title = "Audio Preview",
            backgroundColor = MaterialTheme.extendedColors.elevationSurface3
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (settings.soundEffectsEnabled) GameIcons.Settings.audio else GameIcons.Settings.audioOff,
                    contentDescription = null,
                    tint = if (settings.soundEffectsEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (settings.soundEffectsEnabled) "Audio Enabled" else "Audio Disabled",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (settings.vibrationEnabled) {
                    Icon(
                        imageVector = GameIcons.Settings.vibration,
                        contentDescription = "Vibration enabled",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EnhancedVisualSettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    EnhancedSettingsSection(
        title = "Visual",
        icon = GameIcons.Settings.visual,
        subtitle = "Customize the game's appearance"
    ) {
        // Snake Theme
        EnhancedRadioGroupSetting(
            title = "Snake Theme",
            description = "Choose your snake's color scheme",
            icon = GameIcons.Snake.theme,
            options = SnakeTheme.values().toList(),
            selectedOption = settings.snakeTheme,
            onOptionSelected = { onSettingsChange(settings.copy(snakeTheme = it)) },
            optionDisplay = { it.displayName },
            optionDescription = { "Head and body color combination" }
        )

        EnhancedSwitchSetting(
            title = "Show Grid",
            description = "Display grid lines to help navigate the board",
            checked = settings.showGrid,
            onCheckedChange = { onSettingsChange(settings.copy(showGrid = it)) },
            icon = GameIcons.Snake.grid
        )
        
        // Visual preview
        SettingsPreview(
            title = "Theme Preview",
            backgroundColor = MaterialTheme.extendedColors.elevationSurface3
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Snake preview with selected theme
                ColorPreview(
                    colors = listOf(
                        Color(android.graphics.Color.parseColor(settings.snakeTheme.headColorHex)),
                        Color(android.graphics.Color.parseColor(settings.snakeTheme.bodyColorHex))
                    ),
                    size = 24.dp,
                    spacing = 2.dp
                )
                
                Column {
                    Text(
                        text = settings.snakeTheme.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (settings.showGrid) {
                            Icon(
                                imageVector = GameIcons.Snake.grid,
                                contentDescription = "Grid enabled",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Grid enabled",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "Grid disabled",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedDisplaySettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    EnhancedSettingsSection(
        title = "Display",
        icon = GameIcons.Settings.display,
        subtitle = "Screen and display preferences"
    ) {
        EnhancedSwitchSetting(
            title = "Keep Screen On",
            description = "Prevent screen from turning off during gameplay sessions",
            checked = settings.keepScreenOn,
            onCheckedChange = { onSettingsChange(settings.copy(keepScreenOn = it)) },
            icon = GameIcons.Settings.display
        )
        
        // Display preview
        SettingsPreview(
            title = "Display Status",
            backgroundColor = MaterialTheme.extendedColors.elevationSurface3
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (settings.keepScreenOn) GameIcons.Status.active else GameIcons.Status.inactive,
                    contentDescription = null,
                    tint = if (settings.keepScreenOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (settings.keepScreenOn) "Screen stays on during games" else "Normal screen timeout",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

