package com.advancedsnake.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.advancedsnake.data.local.entities.ScoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    
    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT :limit")
    fun getTopScores(limit: Int = 10): Flow<List<ScoreEntity>>
    
    @Query("SELECT * FROM scores ORDER BY score DESC")
    fun getAllScores(): Flow<List<ScoreEntity>>
    
    @Query("SELECT MAX(score) FROM scores")
    suspend fun getHighestScore(): Int?
    
    @Query("SELECT COUNT(*) FROM scores")
    suspend fun getTotalGamesPlayed(): Int
    
    @Query("SELECT AVG(score) FROM scores")
    suspend fun getAverageScore(): Double?
    
    @Insert
    suspend fun insertScore(score: ScoreEntity)
    
    @Query("DELETE FROM scores")
    suspend fun clearAllScores()
    
    @Query("SELECT * FROM scores WHERE score >= :score ORDER BY score DESC")
    fun getScoresAbove(score: Int): Flow<List<ScoreEntity>>
    
    @Query("SELECT COUNT(*) + 1 FROM scores WHERE score > :score")
    suspend fun getPlayerRank(score: Int): Int
}