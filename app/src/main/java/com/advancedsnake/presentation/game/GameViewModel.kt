package com.advancedsnake.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancedsnake.domain.entities.Direction
import com.advancedsnake.domain.entities.GameState
import com.advancedsnake.domain.repositories.GameRepository
import com.advancedsnake.domain.usecases.ChangeDirectionUseCase
import com.advancedsnake.domain.usecases.InitializeGameUseCase
import com.advancedsnake.domain.usecases.UpdateGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val initializeGameUseCase: InitializeGameUseCase,
    private val updateGameUseCase: UpdateGameUseCase,
    private val changeDirectionUseCase: ChangeDirectionUseCase,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow(initializeGameUseCase(BOARD_WIDTH, BOARD_HEIGHT))
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var gameLoopJob: Job? = null

    init {
        startGameLoop()
    }

    fun onDirectionChange(newDirection: Direction) {
        _gameState.update { currentState ->
            changeDirectionUseCase(currentState, newDirection)
        }
    }

    fun restartGame() {
        gameLoopJob?.cancel()
        viewModelScope.launch {
            gameRepository.saveHighScore(_gameState.value.score)
            _gameState.value = initializeGameUseCase(BOARD_WIDTH, BOARD_HEIGHT)
            startGameLoop()
        }
    }

    private fun startGameLoop() {
        gameLoopJob = viewModelScope.launch {
            while (true) {
                delay(GAME_TICK_MS)
                _gameState.update { currentState ->
                    val updatedState = updateGameUseCase(currentState)
                    if (updatedState.isGameOver) {
                        handleGameOver()
                    }
                    updatedState
                }
            }
        }
    }

    private fun handleGameOver() {
        gameLoopJob?.cancel()
        viewModelScope.launch {
            gameRepository.saveHighScore(_gameState.value.score)
        }
    }

    companion object {
        private const val BOARD_WIDTH = 20
        private const val BOARD_HEIGHT = 30
        private const val GAME_TICK_MS = 150L
    }
}