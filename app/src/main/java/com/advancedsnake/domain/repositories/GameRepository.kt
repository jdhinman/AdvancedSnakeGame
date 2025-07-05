package com.advancedsnake.domain.repositories

interface GameRepository {
    suspend fun getHighScore(): Int
    suspend fun saveHighScore(score: Int)
}