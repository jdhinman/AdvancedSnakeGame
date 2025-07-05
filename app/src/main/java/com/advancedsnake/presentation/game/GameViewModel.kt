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

    private var lastDirectionChangeTime = 0L
    
    fun onDirectionChange(newDirection: Direction) {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastChange = currentTime - lastDirectionChangeTime
        
        // Debounce rapid direction changes at ViewModel level
        if (timeSinceLastChange < 150) return
        
        _gameState.update { currentState ->
            // Prevent invalid direction changes (reverse direction into snake body)
            val currentDirection = currentState.snake.direction
            val isValidDirection = when (newDirection) {
                Direction.UP -> currentDirection != Direction.DOWN
                Direction.DOWN -> currentDirection != Direction.UP
                Direction.LEFT -> currentDirection != Direction.RIGHT
                Direction.RIGHT -> currentDirection != Direction.LEFT
            }
            
            if (isValidDirection) {
                lastDirectionChangeTime = currentTime
                changeDirectionUseCase(currentState, newDirection)
            } else {
                currentState
            }
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
                val currentScore = _gameState.value.score
                val gameTickMs = calculateGameSpeed(currentScore)
                delay(gameTickMs)
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
    
    private fun calculateGameSpeed(score: Int): Long {
        // Start at 350ms (beginner-friendly), decrease by 15ms every 3 points
        val baseSpeed = INITIAL_GAME_SPEED
        val speedDecrease = (score / 3) * SPEED_DECREASE_PER_LEVEL
        val currentSpeed = baseSpeed - speedDecrease
        
        // Cap minimum speed to prevent game becoming unplayable
        return currentSpeed.coerceAtLeast(MIN_GAME_SPEED)
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
        private const val INITIAL_GAME_SPEED = 350L  // Start slower (beginner-friendly)
        private const val SPEED_DECREASE_PER_LEVEL = 15L  // Speed up by 15ms every 3 points
        private const val MIN_GAME_SPEED = 120L  // Minimum speed cap (challenging but playable)
    }
}