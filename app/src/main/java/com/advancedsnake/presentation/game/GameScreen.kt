package com.advancedsnake.presentation.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.advancedsnake.R
import com.advancedsnake.domain.entities.Direction
import com.advancedsnake.domain.entities.GameSettings
import com.advancedsnake.domain.entities.GameState
import com.advancedsnake.domain.entities.Point
import kotlin.math.abs

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val settings by viewModel.currentSettings.collectAsStateWithLifecycle()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Score Display
        ScoreDisplay(
            score = gameState.score,
            modifier = Modifier.padding(16.dp)
        )
        
        // Pause/Resume Button
        if (!gameState.isGameOver) {
            Button(
                onClick = { viewModel.togglePause() },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (gameState.isPaused) "Resume" else "Pause"
                )
            }
        }
        
        // Game Board
        GameBoard(
            gameState = gameState,
            settings = settings,
            onDirectionChange = viewModel::onDirectionChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
        
        // Controls hint
        Text(
            text = "Swipe to control the snake",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
    }
    
    // Game Over Dialog
    if (gameState.isGameOver) {
        GameOverDialog(
            score = gameState.score,
            onRestart = viewModel::restartGame,
            onExit = { navController.popBackStack() }
        )
    }
}

@Composable
private fun ScoreDisplay(
    score: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = stringResource(R.string.score, score),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        )
    }
}

@Composable
private fun GameBoard(
    gameState: GameState,
    settings: GameSettings,
    onDirectionChange: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    
    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }
    var hasDirectionChanged by remember { mutableStateOf(false) }
    var lastDirectionChangeTime by remember { mutableStateOf(0L) }
    
    // Extract settings values for use inside Canvas
    val showGrid = settings.showGrid
    val bodyColor = Color(android.graphics.Color.parseColor(settings.snakeTheme.bodyColorHex))
    val headColor = Color(android.graphics.Color.parseColor(settings.snakeTheme.headColorHex))
    
    Canvas(
        modifier = modifier
            .aspectRatio(gameState.boardWidth.toFloat() / gameState.boardHeight.toFloat())
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStartPosition = offset
                        hasDirectionChanged = false
                    },
                    onDragEnd = {
                        hasDirectionChanged = false
                    },
                    onDrag = { change, _ ->
                        if (!hasDirectionChanged) {
                            val currentTime = System.currentTimeMillis()
                            val timeSinceLastChange = currentTime - lastDirectionChangeTime
                            
                            // Dynamic debouncing based on sensitivity (higher sensitivity = faster response)
                            val debounceTime = (150 / settings.controlSensitivity).toLong().coerceIn(50L, 300L)
                            if (timeSinceLastChange < debounceTime) return@detectDragGestures
                            
                            val totalDrag = change.position - dragStartPosition
                            val threshold = with(density) { 
                                // Adaptive threshold based on screen size and sensitivity
                                val baseThreshold = 60.dp.toPx()
                                val screenAdaptive = maxOf(baseThreshold, size.width * 0.08f, size.height * 0.06f)
                                // Apply sensitivity: higher sensitivity = lower threshold (easier to trigger)
                                screenAdaptive / settings.controlSensitivity
                            }
                            
                            // Dead zone to prevent accidental direction changes
                            val deadZone = with(density) { 20.dp.toPx() }
                            
                            if (abs(totalDrag.x) > deadZone || abs(totalDrag.y) > deadZone) {
                                // Determine primary direction with bias towards intended direction
                                val primaryAxisThreshold = threshold
                                val secondaryAxisThreshold = threshold * 0.6f
                                
                                if (abs(totalDrag.x) > primaryAxisThreshold && 
                                    abs(totalDrag.x) > abs(totalDrag.y) + secondaryAxisThreshold) {
                                    // Clear horizontal gesture
                                    val direction = if (totalDrag.x > 0) Direction.RIGHT else Direction.LEFT
                                    onDirectionChange(direction)
                                    hasDirectionChanged = true
                                    lastDirectionChangeTime = currentTime
                                } else if (abs(totalDrag.y) > primaryAxisThreshold && 
                                           abs(totalDrag.y) > abs(totalDrag.x) + secondaryAxisThreshold) {
                                    // Clear vertical gesture
                                    val direction = if (totalDrag.y > 0) Direction.DOWN else Direction.UP
                                    onDirectionChange(direction)
                                    hasDirectionChanged = true
                                    lastDirectionChangeTime = currentTime
                                }
                            }
                        }
                    }
                )
            }
    ) {
        drawGame(gameState)
    }
}

private fun DrawScope.drawGame(gameState: GameState) {
    val cellWidth = size.width / gameState.boardWidth
    val cellHeight = size.height / gameState.boardHeight
    
    // Draw background
    drawRect(
        color = Color.Black,
        size = size
    )
    
    // Draw grid lines (if enabled in settings)
    if (showGrid) {
        val gridColor = Color.Gray.copy(alpha = 0.3f)
        for (x in 0..gameState.boardWidth) {
            drawLine(
                color = gridColor,
                start = Offset(x * cellWidth, 0f),
                end = Offset(x * cellWidth, size.height),
                strokeWidth = 1.dp.toPx()
            )
        }
        for (y in 0..gameState.boardHeight) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y * cellHeight),
                end = Offset(size.width, y * cellHeight),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
    
    // Draw snake body
    gameState.snake.body.forEach { bodyPart ->
        drawSnakeSegment(bodyPart, cellWidth, cellHeight, bodyColor)
    }
    
    // Draw snake head with enhanced visuals and theme color
    drawSnakeHead(gameState.snake.head, gameState.snake.direction, cellWidth, cellHeight, headColor)
    
    // Draw food
    drawFood(gameState.food.position, cellWidth, cellHeight)
}

private fun DrawScope.drawSnakeHead(
    position: Point,
    direction: Direction,
    cellWidth: Float,
    cellHeight: Float,
    headColor: Color = Color(0xFF2E7D32)
) {
    val padding = 2.dp.toPx()
    val headSize = Size(cellWidth - 2 * padding, cellHeight - 2 * padding)
    val headTopLeft = Offset(
        position.x * cellWidth + padding,
        position.y * cellHeight + padding
    )
    
    // Draw head background with theme color
    drawRect(
        color = headColor,
        topLeft = headTopLeft,
        size = headSize
    )
    
    // Draw eyes
    val eyeRadius = (cellWidth * 0.08f).coerceAtLeast(2f)
    val eyeColor = Color.White
    val pupilColor = Color.Black
    val pupilRadius = eyeRadius * 0.6f
    
    // Calculate eye positions based on direction
    val (leftEyeOffset, rightEyeOffset) = when (direction) {
        Direction.UP -> {
            val eyeY = headTopLeft.y + headSize.height * 0.3f
            Pair(
                Offset(headTopLeft.x + headSize.width * 0.3f, eyeY),
                Offset(headTopLeft.x + headSize.width * 0.7f, eyeY)
            )
        }
        Direction.DOWN -> {
            val eyeY = headTopLeft.y + headSize.height * 0.7f
            Pair(
                Offset(headTopLeft.x + headSize.width * 0.3f, eyeY),
                Offset(headTopLeft.x + headSize.width * 0.7f, eyeY)
            )
        }
        Direction.LEFT -> {
            val eyeX = headTopLeft.x + headSize.width * 0.3f
            Pair(
                Offset(eyeX, headTopLeft.y + headSize.height * 0.3f),
                Offset(eyeX, headTopLeft.y + headSize.height * 0.7f)
            )
        }
        Direction.RIGHT -> {
            val eyeX = headTopLeft.x + headSize.width * 0.7f
            Pair(
                Offset(eyeX, headTopLeft.y + headSize.height * 0.3f),
                Offset(eyeX, headTopLeft.y + headSize.height * 0.7f)
            )
        }
    }
    
    // Draw eyes (white circles with black pupils)
    drawCircle(color = eyeColor, radius = eyeRadius, center = leftEyeOffset)
    drawCircle(color = pupilColor, radius = pupilRadius, center = leftEyeOffset)
    drawCircle(color = eyeColor, radius = eyeRadius, center = rightEyeOffset)
    drawCircle(color = pupilColor, radius = pupilRadius, center = rightEyeOffset)
    
    // Draw directional indicator (small triangle pointing in movement direction)
    val indicatorColor = Color(0xFF1B5E20)
    val indicatorSize = cellWidth * 0.15f
    
    when (direction) {
        Direction.UP -> {
            val tipX = headTopLeft.x + headSize.width * 0.5f
            val tipY = headTopLeft.y + padding
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(tipX, tipY)
                    lineTo(tipX - indicatorSize, tipY + indicatorSize)
                    lineTo(tipX + indicatorSize, tipY + indicatorSize)
                    close()
                },
                color = indicatorColor
            )
        }
        Direction.DOWN -> {
            val tipX = headTopLeft.x + headSize.width * 0.5f
            val tipY = headTopLeft.y + headSize.height - padding
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(tipX, tipY)
                    lineTo(tipX - indicatorSize, tipY - indicatorSize)
                    lineTo(tipX + indicatorSize, tipY - indicatorSize)
                    close()
                },
                color = indicatorColor
            )
        }
        Direction.LEFT -> {
            val tipX = headTopLeft.x + padding
            val tipY = headTopLeft.y + headSize.height * 0.5f
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(tipX, tipY)
                    lineTo(tipX + indicatorSize, tipY - indicatorSize)
                    lineTo(tipX + indicatorSize, tipY + indicatorSize)
                    close()
                },
                color = indicatorColor
            )
        }
        Direction.RIGHT -> {
            val tipX = headTopLeft.x + headSize.width - padding
            val tipY = headTopLeft.y + headSize.height * 0.5f
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(tipX, tipY)
                    lineTo(tipX - indicatorSize, tipY - indicatorSize)
                    lineTo(tipX - indicatorSize, tipY + indicatorSize)
                    close()
                },
                color = indicatorColor
            )
        }
    }
}

private fun DrawScope.drawSnakeSegment(
    position: Point,
    cellWidth: Float,
    cellHeight: Float,
    color: Color
) {
    val padding = 2.dp.toPx()
    drawRect(
        color = color,
        topLeft = Offset(
            position.x * cellWidth + padding,
            position.y * cellHeight + padding
        ),
        size = Size(
            cellWidth - 2 * padding,
            cellHeight - 2 * padding
        )
    )
}

private fun DrawScope.drawFood(
    position: Point,
    cellWidth: Float,
    cellHeight: Float
) {
    val padding = 4.dp.toPx()
    drawRect(
        color = Color(0xFFF44336),
        topLeft = Offset(
            position.x * cellWidth + padding,
            position.y * cellHeight + padding
        ),
        size = Size(
            cellWidth - 2 * padding,
            cellHeight - 2 * padding
        )
    )
}

@Composable
private fun GameOverDialog(
    score: Int,
    onRestart: () -> Unit,
    onExit: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.game_over),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = stringResource(R.string.score, score),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onExit,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Exit")
                    }
                    
                    Button(
                        onClick = onRestart,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.restart))
                    }
                }
            }
        }
    }
}