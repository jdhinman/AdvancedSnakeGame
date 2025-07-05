package com.advancedsnake.data.repositories

import com.advancedsnake.data.local.dao.ScoreDao
import com.advancedsnake.data.local.entities.ScoreEntity
import com.advancedsnake.domain.entities.Score
import com.advancedsnake.domain.repositories.LeaderboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepositoryImpl @Inject constructor(
    private val scoreDao: ScoreDao
) : LeaderboardRepository {
    
    override fun getTopScores(limit: Int): Flow<List<Score>> {
        return scoreDao.getTopScores(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getAllScores(): Flow<List<Score>> {
        return scoreDao.getAllScores().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun saveScore(
        score: Int,
        playerName: String,
        snakeLength: Int,
        gameSpeedLevel: String,
        gameDurationMs: Long
    ) {
        val scoreEntity = ScoreEntity(
            score = score,
            playerName = playerName.take(10), // Limit player name length
            snakeLength = snakeLength,
            gameSpeedLevel = gameSpeedLevel,
            gameDurationMs = gameDurationMs,
            timestamp = System.currentTimeMillis()
        )
        scoreDao.insertScore(scoreEntity)
    }
    
    override suspend fun getHighestScore(): Int {
        return scoreDao.getHighestScore() ?: 0
    }
    
    override suspend fun getTotalGamesPlayed(): Int {
        return scoreDao.getTotalGamesPlayed()
    }
    
    override suspend fun getAverageScore(): Double {
        return scoreDao.getAverageScore() ?: 0.0
    }
    
    override suspend fun clearAllScores() {
        scoreDao.clearAllScores()
    }
    
    override suspend fun getPlayerRank(score: Int): Int {
        return scoreDao.getPlayerRank(score)
    }
    
    private fun ScoreEntity.toDomainModel(): Score {
        return Score(
            id = id,
            score = score,
            playerName = playerName,
            snakeLength = snakeLength,
            gameSpeedLevel = gameSpeedLevel,
            timestamp = timestamp,
            gameDurationMs = gameDurationMs
        )
    }
}