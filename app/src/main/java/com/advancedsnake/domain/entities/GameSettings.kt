package com.advancedsnake.domain.entities

data class GameSettings(
    val gameSpeed: GameSpeed = GameSpeed.NORMAL,
    val boardSize: BoardSize = BoardSize.MEDIUM,
    val controlSensitivity: Float = 1.0f,
    val soundEffectsEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val snakeTheme: SnakeTheme = SnakeTheme.CLASSIC,
    val showGrid: Boolean = true,
    val keepScreenOn: Boolean = true,
    val playerName: String = "Player"
)

enum class GameSpeed(
    val displayName: String,
    val baseSpeedMs: Long,
    val speedDecreaseMs: Long,
    val minSpeedMs: Long
) {
    BEGINNER("Beginner", 500L, 10L, 200L),
    NORMAL("Normal", 350L, 15L, 120L),
    EXPERT("Expert", 250L, 20L, 80L);
    
    fun calculateSpeed(score: Int): Long {
        val speedDecrease = (score / 3) * speedDecreaseMs
        return (baseSpeedMs - speedDecrease).coerceAtLeast(minSpeedMs)
    }
}

enum class BoardSize(
    val displayName: String,
    val width: Int,
    val height: Int
) {
    SMALL("Small", 15, 20),
    MEDIUM("Medium", 20, 30),
    LARGE("Large", 25, 35)
}

enum class SnakeTheme(
    val displayName: String,
    val headColorHex: String,
    val bodyColorHex: String
) {
    CLASSIC("Classic", "#4CAF50", "#81C784"),
    NEON("Neon", "#00E5FF", "#0277BD"),
    FIRE("Fire", "#FF5722", "#FF8A65"),
    PURPLE("Purple", "#9C27B0", "#BA68C8"),
    RAINBOW("Rainbow", "#FF5722", "#FFC107")
}