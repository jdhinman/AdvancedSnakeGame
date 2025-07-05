package com.advancedsnake.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.advancedsnake.domain.entities.*
import com.advancedsnake.domain.repositories.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore(name = "game_settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private object SettingsKeys {
        val GAME_SPEED = stringPreferencesKey("game_speed")
        val BOARD_SIZE = stringPreferencesKey("board_size")
        val CONTROL_SENSITIVITY = floatPreferencesKey("control_sensitivity")
        val SOUND_EFFECTS_ENABLED = booleanPreferencesKey("sound_effects_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val SNAKE_THEME = stringPreferencesKey("snake_theme")
        val SHOW_GRID = booleanPreferencesKey("show_grid")
        val KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
        val PLAYER_NAME = stringPreferencesKey("player_name")
    }

    override fun getGameSettings(): Flow<GameSettings> {
        return context.settingsDataStore.data.map { preferences ->
            GameSettings(
                gameSpeed = GameSpeed.valueOf(
                    preferences[SettingsKeys.GAME_SPEED] ?: GameSpeed.NORMAL.name
                ),
                boardSize = BoardSize.valueOf(
                    preferences[SettingsKeys.BOARD_SIZE] ?: BoardSize.MEDIUM.name
                ),
                controlSensitivity = preferences[SettingsKeys.CONTROL_SENSITIVITY] ?: 1.0f,
                soundEffectsEnabled = preferences[SettingsKeys.SOUND_EFFECTS_ENABLED] ?: true,
                vibrationEnabled = preferences[SettingsKeys.VIBRATION_ENABLED] ?: true,
                snakeTheme = SnakeTheme.valueOf(
                    preferences[SettingsKeys.SNAKE_THEME] ?: SnakeTheme.CLASSIC.name
                ),
                showGrid = preferences[SettingsKeys.SHOW_GRID] ?: true,
                keepScreenOn = preferences[SettingsKeys.KEEP_SCREEN_ON] ?: true,
                playerName = preferences[SettingsKeys.PLAYER_NAME] ?: "Player"
            )
        }
    }

    override suspend fun updateGameSettings(settings: GameSettings) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.GAME_SPEED] = settings.gameSpeed.name
            preferences[SettingsKeys.BOARD_SIZE] = settings.boardSize.name
            preferences[SettingsKeys.CONTROL_SENSITIVITY] = settings.controlSensitivity
            preferences[SettingsKeys.SOUND_EFFECTS_ENABLED] = settings.soundEffectsEnabled
            preferences[SettingsKeys.VIBRATION_ENABLED] = settings.vibrationEnabled
            preferences[SettingsKeys.SNAKE_THEME] = settings.snakeTheme.name
            preferences[SettingsKeys.SHOW_GRID] = settings.showGrid
            preferences[SettingsKeys.KEEP_SCREEN_ON] = settings.keepScreenOn
            preferences[SettingsKeys.PLAYER_NAME] = settings.playerName
        }
    }

    override suspend fun resetToDefaults() {
        context.settingsDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}