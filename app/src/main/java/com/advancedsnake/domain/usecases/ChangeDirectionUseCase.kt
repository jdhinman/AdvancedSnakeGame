package com.advancedsnake.domain.usecases

import com.advancedsnake.domain.entities.Direction
import com.advancedsnake.domain.entities.GameState
import javax.inject.Inject

class ChangeDirectionUseCase @Inject constructor() {
    operator fun invoke(gameState: GameState, newDirection: Direction): GameState {
        if (isOppositeDirection(gameState.snake.direction, newDirection)) {
            return gameState
        }
        return gameState.copy(
            snake = gameState.snake.copy(direction = newDirection)
        )
    }

    private fun isOppositeDirection(current: Direction, new: Direction): Boolean {
        return when (current) {
            Direction.UP -> new == Direction.DOWN
            Direction.DOWN -> new == Direction.UP
            Direction.LEFT -> new == Direction.RIGHT
            Direction.RIGHT -> new == Direction.LEFT
        }
    }
}