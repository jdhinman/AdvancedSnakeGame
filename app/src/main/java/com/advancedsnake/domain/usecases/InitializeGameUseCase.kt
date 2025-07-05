package com.advancedsnake.domain.usecases

import com.advancedsnake.domain.entities.Food
import com.advancedsnake.domain.entities.GameState
import com.advancedsnake.domain.entities.Point
import com.advancedsnake.domain.entities.Snake
import javax.inject.Inject

class InitializeGameUseCase @Inject constructor() {
    operator fun invoke(boardWidth: Int, boardHeight: Int): GameState {
        val snake = Snake(
            head = Point(boardWidth / 2, boardHeight / 2),
            body = emptyList()
        )
        return GameState(
            snake = snake,
            food = generateFood(snake, boardWidth, boardHeight),
            boardWidth = boardWidth,
            boardHeight = boardHeight
        )
    }

    private fun generateFood(snake: Snake, boardWidth: Int, boardHeight: Int): Food {
        var foodPosition: Point
        do {
            foodPosition = Point((0 until boardWidth).random(), (0 until boardHeight).random())
        } while (foodPosition == snake.head || snake.body.contains(foodPosition))
        return Food(foodPosition)
    }
}