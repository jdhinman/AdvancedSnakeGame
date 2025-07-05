package com.advancedsnake.domain.repositories

import com.advancedsnake.domain.entities.GameSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    
    fun getGameSettings(): Flow<GameSettings>
    
    suspend fun updateGameSettings(settings: GameSettings)
    
    suspend fun resetToDefaults()
}