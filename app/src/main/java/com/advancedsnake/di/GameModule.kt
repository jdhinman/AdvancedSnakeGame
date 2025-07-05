package com.advancedsnake.di

import com.advancedsnake.data.repositories.GameRepositoryImpl
import com.advancedsnake.domain.repositories.GameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
}