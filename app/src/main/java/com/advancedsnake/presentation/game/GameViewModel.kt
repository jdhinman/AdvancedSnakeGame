package com.advancedsnake.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancedsnake.domain.entities.Direction
import com.advancedsnake.domain.entities.GameSettings
import com.advancedsnake.domain.entities.GameState
import com.advancedsnake.domain.repositories.GameRepository
import com.advancedsnake.domain.repositories.SettingsRepository
import com.advancedsnake.domain.usecases.ChangeDirectionUseCase
import com.advancedsnake.domain.usecases.InitializeGameUseCase
import com.advancedsnake.domain.usecases.SaveScoreUseCase
import com.advancedsnake.domain.usecases.UpdateGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val initializeGameUseCase: InitializeGameUseCase,
    private val updateGameUseCase: UpdateGameUseCase,
    private val changeDirectionUseCase: ChangeDirectionUseCase,
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository,
    private val saveScoreUseCase: SaveScoreUseCase
) : ViewModel() {

    private val _gameState = MutableStateFlow(initializeGameUseCase(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT))
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    private val _currentSettings = MutableStateFlow(GameSettings())
    val currentSettings: StateFlow<GameSettings> = _currentSettings.asStateFlow()

    private var gameLoopJob: Job? = null
    private var gameStartTime: Long = 0L
    private var lastDirectionChangeTime = 0L

    init {
        loadSettings()
        startGameLoop()
        gameStartTime = System.currentTimeMillis()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getGameSettings().collect { settings ->
                _currentSettings.value = settings
                // Update game state with new board size if needed
                if (settings.boardSize.width != _gameState.value.boardWidth ||
                    settings.boardSize.height != _gameState.value.boardHeight) {
                    _gameState.value = initializeGameUseCase(settings.boardSize.width, settings.boardSize.height)
                    gameStartTime = System.currentTimeMillis()
                }
            }
        }
    }

    fun onDirectionChange(newDirection: Direction) {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastChange = currentTime - lastDirectionChangeTime
        
        // Debounce based on control sensitivity setting
        val debounceTime = (200 / _currentSettings.value.controlSensitivity).toLong()
        if (timeSinceLastChange < debounceTime) return
        
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
            // Save to legacy high score system
            gameRepository.saveHighScore(_gameState.value.score)
            
            // Save to new leaderboard system
            saveGameScore()
            
            // Restart with current settings
            val settings = _currentSettings.value
            _gameState.value = initializeGameUseCase(settings.boardSize.width, settings.boardSize.height)
            gameStartTime = System.currentTimeMillis()
            startGameLoop()
        }
    }

    fun togglePause() {
        _gameState.update { currentState ->
            if (currentState.isGameOver) {
                currentState // Can't pause when game is over
            } else {
                val newPausedState = !currentState.isPaused
                if (newPausedState) {
                    // Pausing the game
                    gameLoopJob?.cancel()
                } else {
                    // Resuming the game
                    startGameLoop()
                }
                currentState.copy(isPaused = newPausedState)
            }
        }
    }

    private fun startGameLoop() {
        gameLoopJob = viewModelScope.launch {
            while (true) {
                val currentState = _gameState.value
                
                // Skip game update if paused or game over
                if (currentState.isPaused || currentState.isGameOver) {
                    delay(100) // Small delay to prevent busy-waiting
                    continue
                }
                
                val gameTickMs = calculateGameSpeed(currentState.score)
                delay(gameTickMs)
                _gameState.update { state ->
                    val updatedState = updateGameUseCase(state)
                    if (updatedState.isGameOver) {
                        handleGameOver()
                    }
                    updatedState
                }
            }
        }
    }
    
    private fun calculateGameSpeed(score: Int): Long {
        val gameSpeed = _currentSettings.value.gameSpeed
        return gameSpeed.calculateSpeed(score)
    }
    
    private suspend fun saveGameScore() {
        val currentState = _gameState.value
        val settings = _currentSettings.value
        val gameDuration = System.currentTimeMillis() - gameStartTime
        
        if (currentState.score > 0) {
            saveScoreUseCase(
                score = currentState.score,
                playerName = settings.playerName,
                snakeLength = currentState.snake.body.size + 1, // Include head
                gameSpeedLevel = settings.gameSpeed.displayName,
                gameDurationMs = gameDuration
            )
        }
    }

    private fun handleGameOver() {
        gameLoopJob?.cancel()
        viewModelScope.launch {
            // Save to legacy high score system
            gameRepository.saveHighScore(_gameState.value.score)
            
            // Save to new leaderboard system
            saveGameScore()
        }
    }

    companion object {
        // Default values when settings are not yet loaded
        private const val DEFAULT_BOARD_WIDTH = 20
        private const val DEFAULT_BOARD_HEIGHT = 30
    }
}