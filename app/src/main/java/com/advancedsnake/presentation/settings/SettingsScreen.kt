package com.advancedsnake.presentation.settings

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(stringResource(R.string.settings)) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = viewModel::showResetDialog) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset to defaults")
                }
            }
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Player Settings
                PlayerSettingsSection(
                    settings = uiState.settings,
                    onSettingsChange = viewModel::updateSettings
                )

                // Gameplay Settings
                GameplaySettingsSection(
                    settings = uiState.settings,
                    onSettingsChange = viewModel::updateSettings
                )

                // Audio & Feedback Settings
                AudioSettingsSection(
                    settings = uiState.settings,
                    onSettingsChange = viewModel::updateSettings
                )

                // Visual Settings
                VisualSettingsSection(
                    settings = uiState.settings,
                    onSettingsChange = viewModel::updateSettings
                )

                // Display Settings
                DisplaySettingsSection(
                    settings = uiState.settings,
                    onSettingsChange = viewModel::updateSettings
                )
            }
        }
    }

    // Reset confirmation dialog
    if (uiState.showResetDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideResetDialog,
            title = { Text("Reset Settings") },
            text = { Text("Reset all settings to their default values? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = viewModel::resetToDefaults) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideResetDialog) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerSettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    SettingsSection(title = "Player") {
        OutlinedTextField(
            value = settings.playerName,
            onValueChange = { newName ->
                onSettingsChange(settings.copy(playerName = newName.take(10)))
            },
            label = { Text("Player Name") },
            supportingText = { Text("Used in leaderboard (max 10 characters)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun GameplaySettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    SettingsSection(title = "Gameplay") {
        // Game Speed
        SettingItem(title = "Game Speed", description = "Controls snake movement speed") {
            Column(modifier = Modifier.selectableGroup()) {
                GameSpeed.values().forEach { speed ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (speed == settings.gameSpeed),
                                onClick = { onSettingsChange(settings.copy(gameSpeed = speed)) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (speed == settings.gameSpeed),
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = speed.displayName)
                            Text(
                                text = "Start: ${speed.baseSpeedMs}ms, Min: ${speed.minSpeedMs}ms",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Board Size
        SettingItem(title = "Board Size", description = "Game board dimensions") {
            Column(modifier = Modifier.selectableGroup()) {
                BoardSize.values().forEach { size ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (size == settings.boardSize),
                                onClick = { onSettingsChange(settings.copy(boardSize = size)) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (size == settings.boardSize),
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "${size.displayName} (${size.width}×${size.height})")
                    }
                }
            }
        }

        // Control Sensitivity
        SettingItem(title = "Control Sensitivity", description = "Swipe gesture sensitivity") {
            Column {
                Slider(
                    value = settings.controlSensitivity,
                    onValueChange = { onSettingsChange(settings.copy(controlSensitivity = it)) },
                    valueRange = 0.5f..2.0f,
                    steps = 5
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Low", style = MaterialTheme.typography.bodySmall)
                    Text("High", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun AudioSettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    SettingsSection(title = "Audio & Feedback") {
        SwitchSettingItem(
            title = "Sound Effects",
            description = "Play sound effects during gameplay",
            checked = settings.soundEffectsEnabled,
            onCheckedChange = { onSettingsChange(settings.copy(soundEffectsEnabled = it)) }
        )

        SwitchSettingItem(
            title = "Vibration",
            description = "Haptic feedback on collision and food consumption",
            checked = settings.vibrationEnabled,
            onCheckedChange = { onSettingsChange(settings.copy(vibrationEnabled = it)) }
        )
    }
}

@Composable
private fun VisualSettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    SettingsSection(title = "Visual") {
        // Snake Theme
        SettingItem(title = "Snake Theme", description = "Snake color scheme") {
            Column(modifier = Modifier.selectableGroup()) {
                SnakeTheme.values().forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (theme == settings.snakeTheme),
                                onClick = { onSettingsChange(settings.copy(snakeTheme = theme)) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (theme == settings.snakeTheme),
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = theme.displayName)
                        Spacer(modifier = Modifier.width(8.dp))
                        // Color preview
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(android.graphics.Color.parseColor(theme.headColorHex)))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(android.graphics.Color.parseColor(theme.bodyColorHex)))
                        )
                    }
                }
            }
        }

        SwitchSettingItem(
            title = "Show Grid",
            description = "Display grid lines on game board",
            checked = settings.showGrid,
            onCheckedChange = { onSettingsChange(settings.copy(showGrid = it)) }
        )
    }
}

@Composable
private fun DisplaySettingsSection(
    settings: GameSettings,
    onSettingsChange: (GameSettings) -> Unit
) {
    SettingsSection(title = "Display") {
        SwitchSettingItem(
            title = "Keep Screen On",
            description = "Prevent screen from turning off during gameplay",
            checked = settings.keepScreenOn,
            onCheckedChange = { onSettingsChange(settings.copy(keepScreenOn = it)) }
        )
    }
}

@Composable
private fun SettingItem(
    title: String,
    description: String? = null,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        content()
    }
}

@Composable
private fun SwitchSettingItem(
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}