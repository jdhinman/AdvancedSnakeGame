package com.advancedsnake.di

import android.content.Context
import androidx.room.Room
import com.advancedsnake.data.local.dao.ScoreDao
import com.advancedsnake.data.local.database.AppDatabase
import com.advancedsnake.data.repositories.GameRepositoryImpl
import com.advancedsnake.data.repositories.LeaderboardRepositoryImpl
import com.advancedsnake.data.repositories.SettingsRepositoryImpl
import com.advancedsnake.domain.repositories.GameRepository
import com.advancedsnake.domain.repositories.LeaderboardRepository
import com.advancedsnake.domain.repositories.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GameModule {

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        gameRepositoryImpl: GameRepositoryImpl
    ): GameRepository
    
    @Binds
    @Singleton
    abstract fun bindLeaderboardRepository(
        leaderboardRepositoryImpl: LeaderboardRepositoryImpl
    ): LeaderboardRepository
    
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build()
        }
        
        @Provides
        fun provideScoreDao(database: AppDatabase): ScoreDao {
            return database.scoreDao()
        }
    }
}