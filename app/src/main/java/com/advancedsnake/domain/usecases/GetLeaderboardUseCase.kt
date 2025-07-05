package com.advancedsnake.domain.usecases

import com.advancedsnake.domain.entities.Score
import com.advancedsnake.domain.repositories.LeaderboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository
) {
    operator fun invoke(limit: Int = 10): Flow<List<Score>> {
        return leaderboardRepository.getTopScores(limit)
    }
}