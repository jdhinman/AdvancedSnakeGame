package com.advancedsnake.domain.usecases

import com.advancedsnake.domain.repositories.LeaderboardRepository
import javax.inject.Inject

class SaveScoreUseCase @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository
) {
    suspend operator fun invoke(
        score: Int,
        playerName: String,
        snakeLength: Int,
        gameSpeedLevel: String,
        gameDurationMs: Long
    ) {
        leaderboardRepository.saveScore(
            score = score,
            playerName = playerName,
            snakeLength = snakeLength,
            gameSpeedLevel = gameSpeedLevel,
            gameDurationMs = gameDurationMs
        )
    }
}