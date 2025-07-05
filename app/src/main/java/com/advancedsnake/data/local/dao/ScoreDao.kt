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
    
    @Query("SELECT COALESCE(MAX(score), 0) FROM scores")
    fun getHighestScore(): Int
    
    @Query("SELECT COUNT(*) FROM scores")
    fun getTotalGamesPlayed(): Int
    
    @Query("SELECT COALESCE(AVG(score), 0.0) FROM scores")
    fun getAverageScore(): Double
    
    @Insert
    fun insertScore(score: ScoreEntity): Long
    
    @Query("DELETE FROM scores")
    fun clearAllScores(): Int
    
    @Query("SELECT * FROM scores WHERE score >= :score ORDER BY score DESC")
    fun getScoresAbove(score: Int): Flow<List<ScoreEntity>>
    
    @Query("SELECT COUNT(*) + 1 FROM scores WHERE score > :score")
    fun getPlayerRank(score: Int): Int
}