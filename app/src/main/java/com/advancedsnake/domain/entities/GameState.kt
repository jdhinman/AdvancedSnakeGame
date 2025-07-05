package com.advancedsnake.domain.entities

data class GameState(
    val snake: Snake,
    val food: Food,
    val boardWidth: Int,
    val boardHeight: Int,
    val score: Int = 0,
    val isGameOver: Boolean = false
)