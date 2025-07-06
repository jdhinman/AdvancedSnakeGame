package com.advancedsnake.presentation.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Enhanced rank indicator with metallic effects for top positions
 */
@Composable
fun EnhancedRankIndicator(
    rank: Int,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    isTopThree: Boolean = rank <= 3,
    showAnimation: Boolean = false
) {
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    
    LaunchedEffect(showAnimation) {
        if (showAnimation) {
            scale.animateTo(1.2f, animationSpec = tween(300))
            rotation.animateTo(360f, animationSpec = tween(600, easing = FastOutSlowInEasing))
            scale.animateTo(1f, animationSpec = tween(200))
        }
    }
    
    val rankColor = when (rank) {
        1 -> MaterialTheme.extendedColors.achievementGold
        2 -> MaterialTheme.extendedColors.achievementSilver
        3 -> MaterialTheme.extendedColors.achievementBronze
        else -> MaterialTheme.colorScheme.secondary
    }
    
    val gradient = if (isTopThree) {
        Brush.radialGradient(
            colors = listOf(
                rankColor.copy(alpha = 0.9f),
                rankColor,
                rankColor.copy(alpha = 0.7f)
            ),
            radius = size.value * 0.8f
        )
    } else {
        Brush.radialGradient(
            colors = listOf(rankColor, rankColor.copy(alpha = 0.8f)),
            radius = size.value * 0.6f
        )
    }
    
    Box(
        modifier = modifier
            .size(size)
            .scale(scale.value)
            .rotate(rotation.value),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow for top three
        if (isTopThree) {
            Box(
                modifier = Modifier
                    .size(size * 1.3f)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                rankColor.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            radius = size.value * 0.8f
                        )
                    )
            )
        }
        
        // Main badge
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(gradient)
                .then(
                    if (isTopThree) {
                        Modifier.border(
                            width = 2.dp,
                            color = Color.White.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (rank <= 3) {
                Icon(
                    imageVector = getRankIcon(rank),
                    contentDescription = "Rank $rank",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.5f)
                )
            } else {
                Text(
                    text = rank.toString(),
                    style = MaterialTheme.typography.extended.leaderboardRank,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (size.value * 0.3f).sp
                )
            }
        }
    }
}

/**
 * Enhanced leaderboard entry with animations and improved styling
 */
@Composable
fun EnhancedLeaderboardEntry(
    rank: Int,
    playerName: String,
    score: Int,
    modifier: Modifier = Modifier,
    gameDetails: String? = null,
    timeDetails: String? = null,
    isCurrentPlayer: Boolean = false,
    isTopThree: Boolean = rank <= 3,
    showEntryAnimation: Boolean = false,
    animationDelay: Int = 0
) {
    val alpha = remember { Animatable(0f) }
    val slideOffset = remember { Animatable(100f) }
    val scale = remember { Animatable(0.8f) }
    
    LaunchedEffect(showEntryAnimation) {
        if (showEntryAnimation) {
            kotlinx.coroutines.delay(animationDelay.toLong())
            launch {
                alpha.animateTo(1f, animationSpec = tween(400))
            }
            launch {
                slideOffset.animateTo(0f, animationSpec = tween(400, easing = FastOutSlowInEasing))
            }
            launch {
                scale.animateTo(1f, animationSpec = tween(400, easing = FastOutSlowInEasing))
            }
        } else {
            alpha.snapTo(1f)
            slideOffset.snapTo(0f)
            scale.snapTo(1f)
        }
    }
    
    val backgroundColor = when {
        isCurrentPlayer -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        isTopThree -> when (rank) {
            1 -> MaterialTheme.extendedColors.achievementGold.copy(alpha = 0.1f)
            2 -> MaterialTheme.extendedColors.achievementSilver.copy(alpha = 0.1f)
            3 -> MaterialTheme.extendedColors.achievementBronze.copy(alpha = 0.1f)
            else -> MaterialTheme.extendedColors.elevationSurface2
        }
        else -> MaterialTheme.extendedColors.elevationSurface1
    }
    
    AnimatedCard(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = slideOffset.value.dp)
            .scale(scale.value),
        elevation = if (isTopThree) 8.dp else 4.dp,
        shape = MaterialTheme.shapes.extended.leaderboardCard,
        backgroundColor = backgroundColor,
        border = isCurrentPlayer,
        borderColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rank indicator
            EnhancedRankIndicator(
                rank = rank,
                isTopThree = isTopThree,
                size = if (isTopThree) 52.dp else 40.dp,
                showAnimation = showEntryAnimation
            )
            
            // Player info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = playerName,
                    style = MaterialTheme.typography.extended.leaderboardPlayer,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = if (isCurrentPlayer) FontWeight.ExtraBold else FontWeight.Bold
                )
                
                if (gameDetails != null) {
                    Text(
                        text = gameDetails,
                        style = MaterialTheme.typography.extended.leaderboardDetails,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                if (timeDetails != null) {
                    Text(
                        text = timeDetails,
                        style = MaterialTheme.typography.extended.leaderboardDetails,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            // Score with animation
            AnimatedCounter(
                targetValue = score,
                textStyle = MaterialTheme.typography.extended.leaderboardScore,
                color = if (isCurrentPlayer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                animationDuration = if (showEntryAnimation) 1000 else 0
            )
        }
    }
}

/**
 * Enhanced statistics card with visual charts
 */
@Composable
fun EnhancedStatisticsCard(
    totalGames: Int,
    averageScore: Double,
    highestScore: Int,
    modifier: Modifier = Modifier,
    winRate: Float? = null,
    longestGame: String? = null,
    showAnimations: Boolean = false
) {
    AnimatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.extended.cardLarge,
        backgroundColor = MaterialTheme.extendedColors.elevationSurface3
    ) {
        Text(
            text = "Game Statistics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Primary statistics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticItem(
                label = "Games Played",
                value = totalGames.toString(),
                icon = GameIcons.Snake.board,
                showAnimation = showAnimations
            )
            StatisticItem(
                label = "Average Score",
                value = String.format("%.1f", averageScore),
                icon = GameIcons.Snake.score,
                showAnimation = showAnimations,
                animationDelay = 200
            )
            StatisticItem(
                label = "Best Score",
                value = highestScore.toString(),
                icon = GameIcons.Achievement.gold,
                showAnimation = showAnimations,
                animationDelay = 400
            )
        }
        
        // Additional statistics if available
        if (winRate != null || longestGame != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (winRate != null) {
                    StatisticItem(
                        label = "Win Rate",
                        value = "${(winRate * 100).toInt()}%",
                        icon = GameIcons.Leaderboard.trend,
                        showAnimation = showAnimations,
                        animationDelay = 600
                    )
                }
                if (longestGame != null) {
                    StatisticItem(
                        label = "Longest Game",
                        value = longestGame,
                        icon = GameIcons.Snake.length,
                        showAnimation = showAnimations,
                        animationDelay = 800
                    )
                }
            }
        }
    }
}

/**
 * Individual statistic item with icon and animation
 */
@Composable
private fun StatisticItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    showAnimation: Boolean = false,
    animationDelay: Int = 0
) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.5f) }
    
    LaunchedEffect(showAnimation) {
        if (showAnimation) {
            kotlinx.coroutines.delay(animationDelay.toLong())
            launch {
                alpha.animateTo(1f, animationSpec = tween(400))
            }
            launch {
                scale.animateTo(1f, animationSpec = tween(400, easing = FastOutSlowInEasing))
            }
        } else {
            alpha.snapTo(1f)
            scale.snapTo(1f)
        }
    }
    
    Column(
        modifier = modifier.scale(scale.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        
        AnimatedCounter(
            targetValue = if (value.contains("%")) {
                value.replace("%", "").toIntOrNull() ?: 0
            } else if (value.all { it.isDigit() || it == '.' }) {
                value.toFloatOrNull()?.toInt() ?: 0
            } else {
                0
            },
            textStyle = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            animationDuration = if (showAnimation) 1000 else 0,
            suffix = if (value.contains("%")) "%" else ""
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Achievement progress indicator with visual progress bar
 */
@Composable
fun AchievementProgress(
    title: String,
    description: String,
    currentProgress: Int,
    maxProgress: Int,
    modifier: Modifier = Modifier,
    icon: ImageVector = GameIcons.Achievement.progress,
    isUnlocked: Boolean = false,
    showAnimation: Boolean = false
) {
    val progress = (currentProgress.toFloat() / maxProgress.toFloat()).coerceIn(0f, 1f)
    val animatedProgress = remember { Animatable(0f) }
    
    LaunchedEffect(showAnimation, progress) {
        if (showAnimation) {
            animatedProgress.animateTo(
                progress,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
        } else {
            animatedProgress.snapTo(progress)
        }
    }
    
    AnimatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = if (isUnlocked) 6.dp else 2.dp,
        backgroundColor = if (isUnlocked) {
            MaterialTheme.extendedColors.achievementGold.copy(alpha = 0.1f)
        } else {
            MaterialTheme.extendedColors.elevationSurface1
        },
        border = isUnlocked,
        borderColor = MaterialTheme.extendedColors.achievementGold
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isUnlocked) {
                    MaterialTheme.extendedColors.achievementGold
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(32.dp)
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.extended.achievementTitle,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isUnlocked) FontWeight.Bold else FontWeight.Medium
                )
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.extended.achievementDescription,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedProgress.value)
                            .background(
                                if (isUnlocked) {
                                    MaterialTheme.extendedColors.achievementGold
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }
                            )
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "$currentProgress / $maxProgress",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            if (isUnlocked) {
                Icon(
                    imageVector = GameIcons.Achievement.unlock,
                    contentDescription = "Unlocked",
                    tint = MaterialTheme.extendedColors.achievementGold,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * Empty state component for leaderboard
 */
@Composable
fun EnhancedEmptyLeaderboard(
    modifier: Modifier = Modifier,
    showAnimation: Boolean = false
) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }
    
    LaunchedEffect(showAnimation) {
        if (showAnimation) {
            launch {
                alpha.animateTo(1f, animationSpec = tween(600))
            }
            launch {
                scale.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
            }
        } else {
            alpha.snapTo(1f)
            scale.snapTo(1f)
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .scale(scale.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated trophy icon
        val infiniteTransition = rememberInfiniteTransition(label = "trophy_animation")
        val rotation by infiniteTransition.animateFloat(
            initialValue = -5f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "rotation"
        )
        
        Icon(
            imageVector = GameIcons.Leaderboard.trophy,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .rotate(rotation),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No High Scores Yet!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Play your first game to set the record and start climbing the leaderboard!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        AnimatedButton(
            onClick = { /* Navigate to game */ },
            text = "Start Playing",
            icon = GameIcons.play,
            isPrimary = true
        )
    }
}