package com.advancedsnake.data.repositories

import com.advancedsnake.data.local.dao.ScoreDao
import com.advancedsnake.data.local.entities.ScoreEntity
import com.advancedsnake.domain.entities.Score
import com.advancedsnake.domain.repositories.LeaderboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
    ) = withContext(Dispatchers.IO) {
        val scoreEntity = ScoreEntity(
            score = score,
            playerName = playerName.take(10), // Limit player name length
            snakeLength = snakeLength,
            gameSpeedLevel = gameSpeedLevel,
            gameDurationMs = gameDurationMs,
            timestamp = System.currentTimeMillis()
        )
        scoreDao.insertScore(scoreEntity)
        Unit
    }
    
    override suspend fun getHighestScore(): Int = withContext(Dispatchers.IO) {
        scoreDao.getHighestScore()
    }
    
    override suspend fun getTotalGamesPlayed(): Int = withContext(Dispatchers.IO) {
        scoreDao.getTotalGamesPlayed()
    }
    
    override suspend fun getAverageScore(): Double = withContext(Dispatchers.IO) {
        scoreDao.getAverageScore()
    }
    
    override suspend fun clearAllScores() = withContext(Dispatchers.IO) {
        scoreDao.clearAllScores()
        Unit
    }
    
    override suspend fun getPlayerRank(score: Int): Int = withContext(Dispatchers.IO) {
        scoreDao.getPlayerRank(score)
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