package com.advancedsnake.presentation.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInFromBottom
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.advancedsnake.R
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MenuScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D1B0F),
                        Color(0xFF1A2F1D),
                        Color(0xFF2E4A32)
                    )
                )
            )
    ) {
        // Animated background pattern
        AnimatedSnakePattern()
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Logo/Title
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800)) + slideInFromBottom()
            ) {
                AnimatedGameLogo()
            }
            
            Spacer(modifier = Modifier.height(80.dp))
            
            // Menu buttons
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 300)) + slideInFromBottom()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Play button (primary)
                    StyledGameButton(
                        text = stringResource(id = R.string.play),
                        icon = Icons.Default.PlayArrow,
                        onClick = { navController.navigate("game") },
                        isPrimary = true
                    )
                    
                    // Leaderboard button
                    StyledGameButton(
                        text = stringResource(id = R.string.leaderboard),
                        icon = Icons.Default.Star,
                        onClick = { navController.navigate("leaderboard") }
                    )
                    
                    // Settings button
                    StyledGameButton(
                        text = stringResource(id = R.string.settings),
                        icon = Icons.Default.Settings,
                        onClick = { navController.navigate("settings") }
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedGameLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_animation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Snake Logo
        Box(
            modifier = Modifier
                .size(120.dp)
                .rotate(rotation * 0.1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(120.dp)
            ) {
                drawSnakeSpiral(this, rotation)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Game Title
        Text(
            text = "ADVANCED",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            ),
            color = Color(0xFF4CAF50),
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "SNAKE GAME",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 8.sp
            ),
            color = Color(0xFF81C784),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StyledGameButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isPrimary: Boolean = false
) {
    val scale = remember { Animatable(1f) }
    val interactionSource = remember { MutableInteractionSource() }
    
    val buttonColors = if (isPrimary) {
        Pair(
            Color(0xFF2E7D32),
            Color(0xFF4CAF50)
        )
    } else {
        Pair(
            Color(0xFF1A2F1D),
            Color(0xFF2E4A32)
        )
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(64.dp)
            .scale(scale.value)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = buttonColors.first
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPrimary) 12.dp else 8.dp
        ),
        shape = RoundedCornerShape(32.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            buttonColors.first,
                            buttonColors.second
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isPrimary) 18.sp else 16.sp
                    ),
                    color = Color.White
                )
            }
        }
    }
    
    // Button press animation
    LaunchedEffect(interactionSource) {
        var isPressed = false
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is androidx.compose.foundation.interaction.PressInteraction.Press -> {
                    if (!isPressed) {
                        isPressed = true
                        scale.animateTo(0.95f, animationSpec = tween(100))
                    }
                }
                is androidx.compose.foundation.interaction.PressInteraction.Release -> {
                    if (isPressed) {
                        isPressed = false
                        scale.animateTo(1f, animationSpec = tween(100))
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedSnakePattern() {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )
    
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.1f)
    ) {
        val width = size.width
        val height = size.height
        
        // Draw subtle snake pattern
        for (i in 0 until 8) {
            val y = height / 8 * i
            val amplitude = 50f
            val frequency = 0.02f
            val phase = animationProgress * 6.28f + i * 0.5f
            
            val path = Path().apply {
                moveTo(0f, y)
                for (x in 0 until width.toInt() step 10) {
                    val waveY = y + amplitude * sin(x * frequency + phase)
                    lineTo(x.toFloat(), waveY)
                }
            }
            
            drawPath(
                path = path,
                color = Color(0xFF4CAF50),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
            )
        }
    }
}

fun drawSnakeSpiral(drawScope: DrawScope, rotation: Float) {
    val center = Offset(drawScope.size.width / 2, drawScope.size.height / 2)
    val radius = drawScope.size.width / 3
    
    // Draw spiral snake
    val segments = 20
    val angleStep = 360f / segments
    
    for (i in 0 until segments) {
        val angle = (i * angleStep + rotation) * (Math.PI / 180f)
        val spiralRadius = radius * (1 - i.toFloat() / segments * 0.8f)
        
        val x = center.x + spiralRadius * cos(angle).toFloat()
        val y = center.y + spiralRadius * sin(angle).toFloat()
        
        val segmentSize = 8f + (segments - i) * 0.5f
        
        drawScope.drawCircle(
            color = Color(0xFF4CAF50),
            radius = segmentSize,
            center = Offset(x, y)
        )
        
        // Add glow effect
        drawScope.drawCircle(
            color = Color(0xFF81C784).copy(alpha = 0.3f),
            radius = segmentSize * 1.5f,
            center = Offset(x, y)
        )
    }
}