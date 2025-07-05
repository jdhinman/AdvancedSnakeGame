package com.advancedsnake.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val score: Int,
    val playerName: String,
    val snakeLength: Int,
    val gameSpeedLevel: String,
    val timestamp: Long = System.currentTimeMillis(),
    val gameDurationMs: Long
)