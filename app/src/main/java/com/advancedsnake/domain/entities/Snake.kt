package com.advancedsnake.domain.entities

data class Point(val x: Int, val y: Int)

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

data class Snake(
    val head: Point,
    val body: List<Point> = emptyList(),
    val direction: Direction = Direction.RIGHT
)