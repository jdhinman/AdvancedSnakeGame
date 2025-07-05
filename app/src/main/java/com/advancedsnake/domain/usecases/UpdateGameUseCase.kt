package com.advancedsnake.domain.usecases

import com.advancedsnake.domain.entities.Direction
import com.advancedsnake.domain.entities.Food
import com.advancedsnake.domain.entities.GameState
import com.advancedsnake.domain.entities.Point
import com.advancedsnake.domain.entities.Snake
import javax.inject.Inject

class UpdateGameUseCase @Inject constructor() {

    operator fun invoke(gameState: GameState): GameState {
        if (gameState.isGameOver) return gameState

        val newHead = moveHead(gameState.snake)
        
        if (isCollision(newHead, gameState.snake, gameState.boardWidth, gameState.boardHeight)) {
            return gameState.copy(isGameOver = true)
        }

        val ateFood = newHead == gameState.food.position

        val newBody = updateBody(gameState.snake, ateFood)

        val newSnake = gameState.snake.copy(
            head = newHead,
            body = newBody
        )

        val newFood = if (ateFood) {
            generateFood(newSnake, gameState.boardWidth, gameState.boardHeight)
        } else {
            gameState.food
        }

        val newScore = if (ateFood) gameState.score + 1 else gameState.score

        return gameState.copy(
            snake = newSnake,
            food = newFood,
            score = newScore
        )
    }

    private fun moveHead(snake: Snake): Point {
        return when (snake.direction) {
            Direction.UP -> snake.head.copy(y = snake.head.y - 1)
            Direction.DOWN -> snake.head.copy(y = snake.head.y + 1)
            Direction.LEFT -> snake.head.copy(x = snake.head.x - 1)
            Direction.RIGHT -> snake.head.copy(x = snake.head.x + 1)
        }
    }

    private fun isCollision(head: Point, snake: Snake, boardWidth: Int, boardHeight: Int): Boolean {
        return head.x < 0 || head.x >= boardWidth ||
               head.y < 0 || head.y >= boardHeight ||
               snake.body.contains(head)
    }

    private fun updateBody(snake: Snake, ateFood: Boolean): List<Point> {
        val newBody = mutableListOf<Point>()
        newBody.add(snake.head)
        newBody.addAll(snake.body)
        if (!ateFood) {
            newBody.removeLast()
        }
        return newBody
    }

    private fun generateFood(snake: Snake, boardWidth: Int, boardHeight: Int): Food {
        var foodPosition: Point
        do {
            foodPosition = Point((0 until boardWidth).random(), (0 until boardHeight).random())
        } while (foodPosition == snake.head || snake.body.contains(foodPosition))
        return Food(foodPosition)
    }
}