package com.advancedsnake.presentation.leaderboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.advancedsnake.R
import com.advancedsnake.domain.entities.Score
import com.advancedsnake.presentation.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                        MaterialTheme.extendedColors.gradientStart,
                        MaterialTheme.extendedColors.gradientMid,
                        MaterialTheme.extendedColors.gradientEnd
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Enhanced Top App Bar
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = GameIcons.Leaderboard.trophy,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.leaderboard),
                            style = MaterialTheme.typography.extended.settingsSection,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                navigationIcon = {
                    AnimatedIconButton(
                        onClick = { navController.popBackStack() },
                        icon = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                },
                actions = {
                    if (uiState.scores.isNotEmpty()) {
                        AnimatedIconButton(
                            onClick = viewModel::showClearDialog,
                            icon = GameIcons.Leaderboard.clear,
                            contentDescription = "Clear scores",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PulsingIndicator(
                            size = 48.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Loading Leaderboard...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else if (uiState.scores.isEmpty()) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInFromBottom()
                ) {
                    EnhancedEmptyLeaderboard(showAnimation = true)
                }
            } else {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInFromBottom()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Enhanced Statistics card
                        item {
                            EnhancedStatisticsCard(
                                totalGames = uiState.totalGamesPlayed,
                                averageScore = uiState.averageScore,
                                highestScore = uiState.scores.firstOrNull()?.score ?: 0,
                                showAnimations = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        
                        // Achievement progress section
                        item {
                            AchievementSection(
                                scores = uiState.scores,
                                totalGames = uiState.totalGamesPlayed
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Enhanced Score items
                        itemsIndexed(uiState.scores) { index, score ->
                            EnhancedLeaderboardEntry(
                                rank = index + 1,
                                playerName = score.playerName,
                                score = score.score,
                                gameDetails = "Snake length: ${score.snakeLength} • ${score.gameSpeedLevel}",
                                timeDetails = "${score.relativeTime} • ${score.formattedDuration}",
                                isTopThree = index < 3,
                                showEntryAnimation = true,
                                animationDelay = index * 100
                            )
                        }
                        
                        // Bottom padding
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }

    // Enhanced Clear confirmation dialog
    if (uiState.showClearDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideClearDialog,
            title = { 
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = GameIcons.Leaderboard.clear,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        "Clear All Scores",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            },
            text = { 
                Text(
                    "Are you sure you want to clear all scores? This will permanently delete your leaderboard history and cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                AnimatedButton(
                    onClick = viewModel::clearAllScores,
                    text = "Clear All",
                    icon = GameIcons.Leaderboard.clear
                )
            },
            dismissButton = {
                AnimatedButton(
                    onClick = viewModel::hideClearDialog,
                    text = "Cancel",
                    icon = GameIcons.UI.cancel
                )
            },
            shape = MaterialTheme.shapes.extended.dialogMedium
        )
    }
}

@Composable
private fun AchievementSection(
    scores: List<Score>,
    totalGames: Int
) {
    AnimatedCard(
        elevation = 6.dp,
        shape = MaterialTheme.shapes.extended.achievementCard,
        backgroundColor = MaterialTheme.extendedColors.elevationSurface2
    ) {
        Text(
            text = "Achievement Progress",
            style = MaterialTheme.typography.extended.achievementTitle,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // First Score achievement
            AchievementProgress(
                title = "First Steps",
                description = "Play your first game",
                currentProgress = if (totalGames > 0) 1 else 0,
                maxProgress = 1,
                icon = GameIcons.Achievement.milestone,
                isUnlocked = totalGames > 0,
                showAnimation = true
            )
            
            // 10 Games achievement
            AchievementProgress(
                title = "Getting Started",
                description = "Play 10 games",
                currentProgress = totalGames.coerceAtMost(10),
                maxProgress = 10,
                icon = GameIcons.Achievement.progress,
                isUnlocked = totalGames >= 10,
                showAnimation = true
            )
            
            // High Score achievement
            val highestScore = scores.firstOrNull()?.score ?: 0
            AchievementProgress(
                title = "High Achiever",
                description = "Reach a score of 100",
                currentProgress = highestScore.coerceAtMost(100),
                maxProgress = 100,
                icon = GameIcons.Achievement.gold,
                isUnlocked = highestScore >= 100,
                showAnimation = true
            )
            
            // Top 3 achievement  
            val topThreeCount = scores.take(3).size
            AchievementProgress(
                title = "Podium Finish",
                description = "Get 3 scores in the leaderboard",
                currentProgress = topThreeCount,
                maxProgress = 3,
                icon = GameIcons.Leaderboard.medal,
                isUnlocked = topThreeCount >= 3,
                showAnimation = true
            )
        }
    }
}

