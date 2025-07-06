package com.advancedsnake.presentation.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
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
import kotlin.math.*
import kotlin.random.Random

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
            // Enhanced Score Display with Animation
        AnimatedScoreDisplay(
            score = gameState.score,
            modifier = Modifier.padding(16.dp)
        )
        
        // Enhanced Pause/Resume Button with Visual Effects
        if (!gameState.isGameOver) {
            AnimatedPauseButton(
                isPaused = gameState.isPaused,
                onTogglePause = { viewModel.togglePause() },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
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
        
        // Enhanced Controls hint with animation
        AnimatedControlsHint(
            modifier = Modifier.padding(16.dp)
        )
    }
    
    // Enhanced Game Over Dialog with Visual Effects
    if (gameState.isGameOver) {
        EnhancedGameOverDialog(
            score = gameState.score,
            onRestart = viewModel::restartGame,
            onExit = { navController.popBackStack() }
        )
    }
}

@Composable
private fun AnimatedScoreDisplay(
    score: Int,
    modifier: Modifier = Modifier
) {
    var previousScore by remember { mutableStateOf(score) }
    val scoreAnimation = animateIntAsState(
        targetValue = score,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "score_animation"
    )
    
    // Pulse animation when score changes
    val pulseAnimation = remember { Animatable(1f) }
    LaunchedEffect(score) {
        if (score > previousScore) {
            pulseAnimation.animateTo(
                targetValue = 1.15f,
                animationSpec = tween(durationMillis = 150)
            )
            pulseAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 150)
            )
        }
        previousScore = score
    }
    
    Card(
        modifier = modifier.scale(pulseAnimation.value),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Background glow effect
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4CAF50).copy(alpha = 0.3f),
                            Color.Transparent
                        ),
                        radius = size.minDimension * 0.8f
                    ),
                    center = center,
                    radius = size.minDimension * 0.6f
                )
            }
            
            Text(
                text = stringResource(R.string.score, scoreAnimation.value),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }
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
    
    // Animation states for visual effects
    val infiniteTransition = rememberInfiniteTransition(label = "game_effects")
    val backgroundPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "background_pulse"
    )
    
    val sparkleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkle_rotation"
    )
    
    // Particle system state
    val particleSystem = remember { ParticleSystem() }
    
    // Touch handling state
    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }
    var hasDirectionChanged by remember { mutableStateOf(false) }
    var lastDirectionChangeTime by remember { mutableStateOf(0L) }
    
    // Extract settings
    val showGridLines = settings.showGrid
    val snakeBodyThemeColor = Color(android.graphics.Color.parseColor(settings.snakeTheme.bodyColorHex))
    val snakeHeadThemeColor = Color(android.graphics.Color.parseColor(settings.snakeTheme.headColorHex))
    
    // Track score changes for particle effects
    var lastScore by remember { mutableStateOf(gameState.score) }
    LaunchedEffect(gameState.score) {
        if (gameState.score > lastScore) {
            // Add celebration particles when score increases
            repeat(15) {
                particleSystem.addParticle(
                    ParticleType.CELEBRATION,
                    gameState.food.position
                )
            }
        }
        lastScore = gameState.score
    }
    
    // Update particle system
    LaunchedEffect(Unit) {
        while (true) {
            particleSystem.update()
            kotlinx.coroutines.delay(16) // ~60fps
        }
    }
    
    Canvas(
        modifier = modifier
            .aspectRatio(gameState.boardWidth.toFloat() / gameState.boardHeight.toFloat())
            .clip(RoundedCornerShape(12.dp))
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
                            
                            val debounceTime = (150 / settings.controlSensitivity).toLong().coerceIn(50L, 300L)
                            if (timeSinceLastChange < debounceTime) return@detectDragGestures
                            
                            val totalDrag = change.position - dragStartPosition
                            val threshold = with(density) { 
                                val baseThreshold = 60.dp.toPx()
                                val screenAdaptive = maxOf(baseThreshold, size.width * 0.08f, size.height * 0.06f)
                                screenAdaptive / settings.controlSensitivity
                            }
                            
                            val deadZone = with(density) { 20.dp.toPx() }
                            
                            if (abs(totalDrag.x) > deadZone || abs(totalDrag.y) > deadZone) {
                                val primaryAxisThreshold = threshold
                                val secondaryAxisThreshold = threshold * 0.6f
                                
                                if (abs(totalDrag.x) > primaryAxisThreshold && 
                                    abs(totalDrag.x) > abs(totalDrag.y) + secondaryAxisThreshold) {
                                    val direction = if (totalDrag.x > 0) Direction.RIGHT else Direction.LEFT
                                    onDirectionChange(direction)
                                    hasDirectionChanged = true
                                    lastDirectionChangeTime = currentTime
                                } else if (abs(totalDrag.y) > primaryAxisThreshold && 
                                           abs(totalDrag.y) > abs(totalDrag.x) + secondaryAxisThreshold) {
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
        renderEnhancedGameCanvas(
            gameState = gameState,
            showGridLines = showGridLines,
            snakeBodyThemeColor = snakeBodyThemeColor,
            snakeHeadThemeColor = snakeHeadThemeColor,
            backgroundPulse = backgroundPulse,
            sparkleRotation = sparkleRotation,
            particleSystem = particleSystem
        )
    }
}

// Enhanced particle system for visual effects
class ParticleSystem {
    private val particles = mutableListOf<Particle>()
    
    fun addParticle(type: ParticleType, position: Point) {
        particles.add(Particle(type, position))
    }
    
    fun update() {
        particles.removeAll { it.isDead() }
        particles.forEach { it.update() }
    }
    
    fun getParticles(): List<Particle> = particles
}

class Particle(
    private val type: ParticleType,
    private val startPosition: Point
) {
    private val startTime = System.currentTimeMillis()
    private val lifetime = when (type) {
        ParticleType.CELEBRATION -> 1000L
        ParticleType.FOOD_GLOW -> 2000L
        ParticleType.TRAIL -> 500L
    }
    
    private val velocity = when (type) {
        ParticleType.CELEBRATION -> Offset(
            Random.nextFloat() * 4 - 2,
            Random.nextFloat() * 4 - 2
        )
        ParticleType.FOOD_GLOW -> Offset.Zero
        ParticleType.TRAIL -> Offset.Zero
    }
    
    fun update() {
        // Particle physics updates here
    }
    
    fun isDead(): Boolean = System.currentTimeMillis() - startTime > lifetime
    
    fun getPosition(): Offset {
        val elapsed = System.currentTimeMillis() - startTime
        return when (type) {
            ParticleType.CELEBRATION -> Offset(
                startPosition.x.toFloat() + velocity.x * elapsed / 100f,
                startPosition.y.toFloat() + velocity.y * elapsed / 100f
            )
            else -> Offset(startPosition.x.toFloat(), startPosition.y.toFloat())
        }
    }
    
    fun getAlpha(): Float {
        val elapsed = System.currentTimeMillis() - startTime
        return (1f - elapsed.toFloat() / lifetime).coerceIn(0f, 1f)
    }
    
    fun getType(): ParticleType = type
}

enum class ParticleType {
    CELEBRATION, FOOD_GLOW, TRAIL
}

private fun DrawScope.renderEnhancedGameCanvas(
    gameState: GameState,
    showGridLines: Boolean,
    snakeBodyThemeColor: Color,
    snakeHeadThemeColor: Color,
    backgroundPulse: Float,
    sparkleRotation: Float,
    particleSystem: ParticleSystem
) {
    val cellWidth = size.width / gameState.boardWidth
    val cellHeight = size.height / gameState.boardHeight
    
    // Draw enhanced background with depth and texture
    drawEnhancedBackground(backgroundPulse)
    
    // Draw floating background particles
    drawBackgroundParticles(sparkleRotation)
    
    // Draw grid lines with enhanced styling
    if (showGridLines) {
        drawStylizedGrid(gameState.boardWidth, gameState.boardHeight, cellWidth, cellHeight)
    }
    
    // Draw snake body with enhanced visuals
    gameState.snake.body.forEachIndexed { index, bodyPart ->
        drawEnhancedSnakeSegment(
            bodyPart, 
            cellWidth, 
            cellHeight, 
            snakeBodyThemeColor,
            index,
            gameState.snake.body.size
        )
    }
    
    // Draw snake head with 3D effects
    drawEnhanced3DSnakeHead(
        gameState.snake.head, 
        gameState.snake.direction, 
        cellWidth, 
        cellHeight, 
        snakeHeadThemeColor
    )
    
    // Draw animated food with glow effects
    drawEnhancedAnimatedFood(
        gameState.food.position, 
        cellWidth, 
        cellHeight, 
        backgroundPulse
    )
    
    // Draw particle effects
    drawParticleEffects(particleSystem, cellWidth, cellHeight)
}

private fun DrawScope.drawEnhancedBackground(backgroundPulse: Float) {
    // Create a dynamic gradient background
    val darkColor = Color(0xFF0A0A0A)
    val accentColor = Color(0xFF1A1A2E)
    
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                accentColor.copy(alpha = 0.3f + backgroundPulse * 0.2f),
                darkColor,
                Color.Black
            ),
            center = Offset(size.width * 0.5f, size.height * 0.3f),
            radius = size.minDimension * 0.8f
        ),
        size = size
    )
    
    // Add subtle texture overlay
    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.02f),
                Color.Transparent,
                Color.White.copy(alpha = 0.01f)
            ),
            start = Offset(0f, 0f),
            end = Offset(size.width, size.height)
        ),
        size = size
    )
}

private fun DrawScope.drawBackgroundParticles(sparkleRotation: Float) {
    // Draw subtle floating particles in the background
    val particleCount = 8
    val particleColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
    
    repeat(particleCount) { i ->
        val angle = (i * 360f / particleCount) + sparkleRotation
        val radius = size.minDimension * 0.3f
        val x = size.width * 0.5f + cos(angle * PI / 180f).toFloat() * radius
        val y = size.height * 0.5f + sin(angle * PI / 180f).toFloat() * radius
        
        drawCircle(
            color = particleColor,
            radius = 2f + sin(sparkleRotation * PI / 180f).toFloat().absoluteValue * 3f,
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawStylizedGrid(
    boardWidth: Int,
    boardHeight: Int,
    cellWidth: Float,
    cellHeight: Float
) {
    val gridColor = Color(0xFF2D2D2D).copy(alpha = 0.4f)
    val strokeWidth = 0.5.dp.toPx()
    
    // Draw vertical lines with subtle glow
    for (x in 0..boardWidth) {
        drawLine(
            color = gridColor,
            start = Offset(x * cellWidth, 0f),
            end = Offset(x * cellWidth, size.height),
            strokeWidth = strokeWidth
        )
    }
    
    // Draw horizontal lines with subtle glow
    for (y in 0..boardHeight) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y * cellHeight),
            end = Offset(size.width, y * cellHeight),
            strokeWidth = strokeWidth
        )
    }
}

private fun DrawScope.drawEnhanced3DSnakeHead(
    position: Point,
    direction: Direction,
    cellWidth: Float,
    cellHeight: Float,
    headColor: Color
) {
    val padding = 1.dp.toPx()
    val headSize = Size(cellWidth - 2 * padding, cellHeight - 2 * padding)
    val headTopLeft = Offset(
        position.x * cellWidth + padding,
        position.y * cellHeight + padding
    )
    
    // Draw shadow for 3D effect
    val shadowOffset = Offset(2f, 2f)
    drawRoundRect(
        color = Color.Black.copy(alpha = 0.3f),
        topLeft = headTopLeft + shadowOffset,
        size = headSize,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(headSize.minDimension * 0.2f)
    )
    
    // Draw head background with gradient for 3D effect
    drawRoundRect(
        brush = Brush.radialGradient(
            colors = listOf(
                headColor.copy(alpha = 1f),
                headColor.copy(alpha = 0.8f),
                headColor.copy(alpha = 0.6f)
            ),
            center = headTopLeft + Offset(headSize.width * 0.3f, headSize.height * 0.3f),
            radius = headSize.minDimension * 0.8f
        ),
        topLeft = headTopLeft,
        size = headSize,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(headSize.minDimension * 0.2f)
    )
    
    // Draw highlight for 3D effect
    drawRoundRect(
        color = Color.White.copy(alpha = 0.3f),
        topLeft = headTopLeft + Offset(headSize.width * 0.1f, headSize.height * 0.1f),
        size = Size(headSize.width * 0.6f, headSize.height * 0.6f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(headSize.minDimension * 0.15f)
    )
    
    // Draw enhanced eyes with animation
    val eyeRadius = (cellWidth * 0.1f).coerceAtLeast(3f)
    val eyeColor = Color.White
    val pupilColor = Color.Black
    val pupilRadius = eyeRadius * 0.6f
    
    // Calculate eye positions based on direction
    val (leftEyeOffset, rightEyeOffset) = when (direction) {
        Direction.UP -> {
            val eyeY = headTopLeft.y + headSize.height * 0.35f
            Pair(
                Offset(headTopLeft.x + headSize.width * 0.35f, eyeY),
                Offset(headTopLeft.x + headSize.width * 0.65f, eyeY)
            )
        }
        Direction.DOWN -> {
            val eyeY = headTopLeft.y + headSize.height * 0.65f
            Pair(
                Offset(headTopLeft.x + headSize.width * 0.35f, eyeY),
                Offset(headTopLeft.x + headSize.width * 0.65f, eyeY)
            )
        }
        Direction.LEFT -> {
            val eyeX = headTopLeft.x + headSize.width * 0.35f
            Pair(
                Offset(eyeX, headTopLeft.y + headSize.height * 0.35f),
                Offset(eyeX, headTopLeft.y + headSize.height * 0.65f)
            )
        }
        Direction.RIGHT -> {
            val eyeX = headTopLeft.x + headSize.width * 0.65f
            Pair(
                Offset(eyeX, headTopLeft.y + headSize.height * 0.35f),
                Offset(eyeX, headTopLeft.y + headSize.height * 0.65f)
            )
        }
    }
    
    // Draw eyes with glow effect
    drawCircle(color = eyeColor.copy(alpha = 0.3f), radius = eyeRadius * 1.2f, center = leftEyeOffset)
    drawCircle(color = eyeColor, radius = eyeRadius, center = leftEyeOffset)
    drawCircle(color = pupilColor, radius = pupilRadius, center = leftEyeOffset)
    drawCircle(color = Color.White.copy(alpha = 0.6f), radius = pupilRadius * 0.3f, center = leftEyeOffset + Offset(-1f, -1f))
    
    drawCircle(color = eyeColor.copy(alpha = 0.3f), radius = eyeRadius * 1.2f, center = rightEyeOffset)
    drawCircle(color = eyeColor, radius = eyeRadius, center = rightEyeOffset)
    drawCircle(color = pupilColor, radius = pupilRadius, center = rightEyeOffset)
    drawCircle(color = Color.White.copy(alpha = 0.6f), radius = pupilRadius * 0.3f, center = rightEyeOffset + Offset(-1f, -1f))
}

private fun DrawScope.drawEnhancedSnakeSegment(
    position: Point,
    cellWidth: Float,
    cellHeight: Float,
    color: Color,
    segmentIndex: Int,
    totalSegments: Int
) {
    val padding = 1.5.dp.toPx()
    val segmentSize = Size(cellWidth - 2 * padding, cellHeight - 2 * padding)
    val segmentTopLeft = Offset(
        position.x * cellWidth + padding,
        position.y * cellHeight + padding
    )
    
    // Create gradient effect based on segment position
    val alpha = 0.6f + (segmentIndex.toFloat() / totalSegments) * 0.4f
    val segmentColor = color.copy(alpha = alpha)
    
    // Draw shadow
    drawRoundRect(
        color = Color.Black.copy(alpha = 0.2f),
        topLeft = segmentTopLeft + Offset(1f, 1f),
        size = segmentSize,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(segmentSize.minDimension * 0.15f)
    )
    
    // Draw segment with gradient
    drawRoundRect(
        brush = Brush.radialGradient(
            colors = listOf(
                segmentColor,
                segmentColor.copy(alpha = alpha * 0.8f)
            ),
            center = segmentTopLeft + Offset(segmentSize.width * 0.3f, segmentSize.height * 0.3f),
            radius = segmentSize.minDimension * 0.6f
        ),
        topLeft = segmentTopLeft,
        size = segmentSize,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(segmentSize.minDimension * 0.15f)
    )
    
    // Draw highlight
    drawRoundRect(
        color = Color.White.copy(alpha = 0.15f),
        topLeft = segmentTopLeft + Offset(segmentSize.width * 0.1f, segmentSize.height * 0.1f),
        size = Size(segmentSize.width * 0.5f, segmentSize.height * 0.5f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(segmentSize.minDimension * 0.1f)
    )
}

private fun DrawScope.drawEnhancedAnimatedFood(
    position: Point,
    cellWidth: Float,
    cellHeight: Float,
    animationValue: Float
) {
    val padding = 2.dp.toPx()
    val foodSize = Size(cellWidth - 2 * padding, cellHeight - 2 * padding)
    val foodTopLeft = Offset(
        position.x * cellWidth + padding,
        position.y * cellHeight + padding
    )
    
    val pulseScale = 1f + animationValue * 0.15f
    val scaledSize = Size(
        foodSize.width * pulseScale,
        foodSize.height * pulseScale
    )
    val scaledTopLeft = Offset(
        foodTopLeft.x - (scaledSize.width - foodSize.width) / 2,
        foodTopLeft.y - (scaledSize.height - foodSize.height) / 2
    )
    
    // Draw outer glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFF5722).copy(alpha = 0.3f),
                Color(0xFFFF5722).copy(alpha = 0.1f),
                Color.Transparent
            ),
            center = scaledTopLeft + Offset(scaledSize.width / 2, scaledSize.height / 2),
            radius = scaledSize.minDimension * 0.8f
        ),
        center = scaledTopLeft + Offset(scaledSize.width / 2, scaledSize.height / 2),
        radius = scaledSize.minDimension * 0.7f
    )
    
    // Draw apple-like food
    val center = scaledTopLeft + Offset(scaledSize.width / 2, scaledSize.height / 2)
    
    // Apple body
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFF4444),
                Color(0xFFCC0000),
                Color(0xFF990000)
            ),
            center = center + Offset(-scaledSize.width * 0.1f, -scaledSize.height * 0.1f),
            radius = scaledSize.minDimension * 0.4f
        ),
        topLeft = scaledTopLeft,
        size = scaledSize
    )
    
    // Apple highlight
    drawOval(
        color = Color.White.copy(alpha = 0.4f),
        topLeft = scaledTopLeft + Offset(scaledSize.width * 0.2f, scaledSize.height * 0.1f),
        size = Size(scaledSize.width * 0.3f, scaledSize.height * 0.4f)
    )
    
    // Apple stem
    drawRect(
        color = Color(0xFF4CAF50),
        topLeft = center + Offset(-scaledSize.width * 0.05f, -scaledSize.height * 0.4f),
        size = Size(scaledSize.width * 0.1f, scaledSize.height * 0.2f)
    )
    
    // Sparkle effects
    repeat(4) { i ->
        val angle = (i * 90f) + animationValue * 180f
        val sparkleRadius = scaledSize.minDimension * 0.6f
        val sparkleX = center.x + cos(angle * PI / 180f).toFloat() * sparkleRadius
        val sparkleY = center.y + sin(angle * PI / 180f).toFloat() * sparkleRadius
        
        drawCircle(
            color = Color.White.copy(alpha = 0.6f * (1f - animationValue)),
            radius = 1f + animationValue * 2f,
            center = Offset(sparkleX, sparkleY)
        )
    }
}

private fun DrawScope.drawParticleEffects(
    particleSystem: ParticleSystem,
    cellWidth: Float,
    cellHeight: Float
) {
    particleSystem.getParticles().forEach { particle ->
        val position = particle.getPosition()
        val alpha = particle.getAlpha()
        
        when (particle.getType()) {
            ParticleType.CELEBRATION -> {
                drawCircle(
                    color = Color(0xFFFFD700).copy(alpha = alpha),
                    radius = 3f,
                    center = Offset(
                        position.x * cellWidth + cellWidth / 2,
                        position.y * cellHeight + cellHeight / 2
                    )
                )
            }
            ParticleType.FOOD_GLOW -> {
                drawCircle(
                    color = Color(0xFFFF5722).copy(alpha = alpha * 0.3f),
                    radius = 8f,
                    center = Offset(
                        position.x * cellWidth + cellWidth / 2,
                        position.y * cellHeight + cellHeight / 2
                    )
                )
            }
            ParticleType.TRAIL -> {
                drawCircle(
                    color = Color(0xFF4CAF50).copy(alpha = alpha * 0.5f),
                    radius = 2f,
                    center = Offset(
                        position.x * cellWidth + cellWidth / 2,
                        position.y * cellHeight + cellHeight / 2
                    )
                )
            }
        }
    }
}

@Composable
private fun AnimatedPauseButton(
    isPaused: Boolean,
    onTogglePause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonScale by animateFloatAsState(
        targetValue = if (isPaused) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "button_scale"
    )
    
    val buttonColors = if (isPaused) {
        ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White
        )
    }
    
    Button(
        onClick = onTogglePause,
        modifier = modifier.scale(buttonScale),
        colors = buttonColors,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = if (isPaused) "Resume" else "Pause",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AnimatedControlsHint(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "controls_hint")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hint_alpha"
    )
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.TouchApp,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Swipe to control the snake",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha)
        )
    }
}

@Composable
private fun EnhancedGameOverDialog(
    score: Int,
    onRestart: () -> Unit,
    onExit: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        showDialog = true
    }
    
    if (showDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "game_over_effects")
            val celebrationRotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "celebration_rotation"
            )
            
            Card(
                modifier = Modifier.padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            ) {
                Box {
                    // Background celebration effect
                    Canvas(modifier = Modifier.matchParentSize()) {
                        repeat(8) { i ->
                            val angle = (i * 45f) + celebrationRotation
                            val radius = size.minDimension * 0.3f
                            val x = size.width * 0.5f + cos(angle * PI / 180f).toFloat() * radius
                            val y = size.height * 0.5f + sin(angle * PI / 180f).toFloat() * radius
                            
                            drawCircle(
                                color = Color(0xFFFFD700).copy(alpha = 0.3f),
                                radius = 3f,
                                center = Offset(x, y)
                            )
                        }
                    }
                    
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.game_over),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.3f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 6f
                                )
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Enhanced score display
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.score, score),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedButton(
                                onClick = onExit,
                                modifier = Modifier.weight(1f),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("Exit")
                                }
                            }
                            
                            Button(
                                onClick = onRestart,
                                modifier = Modifier.weight(1f),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50)
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(stringResource(R.string.restart))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}