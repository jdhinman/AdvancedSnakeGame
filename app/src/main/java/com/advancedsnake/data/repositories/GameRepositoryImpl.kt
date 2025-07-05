package com.advancedsnake.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.advancedsnake.domain.repositories.GameRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class GameRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GameRepository {

    private object PreferencesKeys {
        val HIGH_SCORE = intPreferencesKey("high_score")
    }

    override suspend fun getHighScore(): Int {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.HIGH_SCORE] ?: 0
            }.first()
    }

    override suspend fun saveHighScore(score: Int) {
        val currentHighScore = getHighScore()
        if (score > currentHighScore) {
            context.dataStore.edit { settings ->
                settings[PreferencesKeys.HIGH_SCORE] = score
            }
        }
    }
}