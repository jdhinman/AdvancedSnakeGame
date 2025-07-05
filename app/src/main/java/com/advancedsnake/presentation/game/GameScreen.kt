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
import com.advancedsnake.domain.entities.GameState
import com.advancedsnake.domain.entities.Point
import kotlin.math.abs

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    
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
        
        // Game Board
        GameBoard(
            gameState = gameState,
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
    onDirectionChange: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    
    Canvas(
        modifier = modifier
            .aspectRatio(gameState.boardWidth.toFloat() / gameState.boardHeight.toFloat())
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = { },
                    onDrag = { _, dragAmount ->
                        val threshold = with(density) { 50.dp.toPx() }
                        
                        if (abs(dragAmount.x) > abs(dragAmount.y)) {
                            // Horizontal swipe
                            if (abs(dragAmount.x) > threshold) {
                                if (dragAmount.x > 0) {
                                    onDirectionChange(Direction.RIGHT)
                                } else {
                                    onDirectionChange(Direction.LEFT)
                                }
                            }
                        } else {
                            // Vertical swipe
                            if (abs(dragAmount.y) > threshold) {
                                if (dragAmount.y > 0) {
                                    onDirectionChange(Direction.DOWN)
                                } else {
                                    onDirectionChange(Direction.UP)
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
    
    // Draw grid lines (optional, for better visibility)
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
    
    // Draw snake body
    gameState.snake.body.forEach { bodyPart ->
        drawSnakeSegment(bodyPart, cellWidth, cellHeight, Color(0xFF4CAF50))
    }
    
    // Draw snake head
    drawSnakeSegment(gameState.snake.head, cellWidth, cellHeight, Color(0xFF2E7D32))
    
    // Draw food
    drawFood(gameState.food.position, cellWidth, cellHeight)
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