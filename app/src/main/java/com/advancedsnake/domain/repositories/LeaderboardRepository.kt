package com.advancedsnake.domain.repositories

import com.advancedsnake.domain.entities.Score
import kotlinx.coroutines.flow.Flow

interface LeaderboardRepository {
    
    fun getTopScores(limit: Int = 10): Flow<List<Score>>
    
    fun getAllScores(): Flow<List<Score>>
    
    suspend fun saveScore(
        score: Int,
        playerName: String,
        snakeLength: Int,
        gameSpeedLevel: String,
        gameDurationMs: Long
    )
    
    suspend fun getHighestScore(): Int
    
    suspend fun getTotalGamesPlayed(): Int
    
    suspend fun getAverageScore(): Double
    
    suspend fun clearAllScores()
    
    suspend fun getPlayerRank(score: Int): Int
}