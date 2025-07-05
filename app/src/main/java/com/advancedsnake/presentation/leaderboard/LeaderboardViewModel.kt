package com.advancedsnake.presentation.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancedsnake.domain.entities.Score
import com.advancedsnake.domain.repositories.LeaderboardRepository
import com.advancedsnake.domain.usecases.GetLeaderboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LeaderboardUiState(
    val scores: List<Score> = emptyList(),
    val isLoading: Boolean = true,
    val totalGamesPlayed: Int = 0,
    val averageScore: Double = 0.0,
    val showClearDialog: Boolean = false
)

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getLeaderboardUseCase: GetLeaderboardUseCase,
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        loadLeaderboard()
        loadStatistics()
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            getLeaderboardUseCase().collect { scores ->
                _uiState.value = _uiState.value.copy(
                    scores = scores,
                    isLoading = false
                )
            }
        }
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            val totalGames = leaderboardRepository.getTotalGamesPlayed()
            val averageScore = leaderboardRepository.getAverageScore()
            
            _uiState.value = _uiState.value.copy(
                totalGamesPlayed = totalGames,
                averageScore = averageScore
            )
        }
    }

    fun showClearDialog() {
        _uiState.value = _uiState.value.copy(showClearDialog = true)
    }

    fun hideClearDialog() {
        _uiState.value = _uiState.value.copy(showClearDialog = false)
    }

    fun clearAllScores() {
        viewModelScope.launch {
            leaderboardRepository.clearAllScores()
            _uiState.value = _uiState.value.copy(showClearDialog = false)
        }
    }
}