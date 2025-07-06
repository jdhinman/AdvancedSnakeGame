package com.advancedsnake.presentation.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Enhanced animated card component with modern styling
 */
@Composable
fun AnimatedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    elevation: Dp = 4.dp,
    shape: RoundedCornerShape = MaterialTheme.shapes.extended.cardMedium,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: Boolean = false,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    animationDuration: Int = 200,
    content: @Composable ColumnScope.() -> Unit
) {
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }
    val interactionSource = remember { MutableInteractionSource() }
    
    // Handle interaction animations
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is androidx.compose.foundation.interaction.PressInteraction.Press -> {
                    scale.animateTo(0.95f, animationSpec = tween(animationDuration))
                }
                is androidx.compose.foundation.interaction.PressInteraction.Release -> {
                    scale.animateTo(1f, animationSpec = tween(animationDuration))
                }
                is androidx.compose.foundation.interaction.PressInteraction.Cancel -> {
                    scale.animateTo(1f, animationSpec = tween(animationDuration))
                }
            }
        }
    }
    
    // Handle enabled state
    LaunchedEffect(enabled) {
        alpha.animateTo(if (enabled) 1f else 0.6f, animationSpec = tween(animationDuration))
    }
    
    Card(
        modifier = modifier
            .scale(scale.value)
            .alpha(alpha.value)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(),
                        enabled = enabled,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (border) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = shape
                    )
                } else {
                    Modifier
                }
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

/**
 * Enhanced animated button with consistent styling
 */
@Composable
fun AnimatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPrimary: Boolean = false,
    text: String,
    icon: ImageVector? = null,
    iconPosition: ButtonIconPosition = ButtonIconPosition.START,
    shape: RoundedCornerShape = MaterialTheme.shapes.extended.buttonMedium,
    animationDuration: Int = 150,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
) {
    val scale = remember { Animatable(1f) }
    val interactionSource = remember { MutableInteractionSource() }
    
    val buttonColors = if (isPrimary) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    }
    
    // Handle press animations
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is androidx.compose.foundation.interaction.PressInteraction.Press -> {
                    scale.animateTo(0.95f, animationSpec = tween(animationDuration))
                }
                is androidx.compose.foundation.interaction.PressInteraction.Release -> {
                    scale.animateTo(1f, animationSpec = tween(animationDuration))
                }
                is androidx.compose.foundation.interaction.PressInteraction.Cancel -> {
                    scale.animateTo(1f, animationSpec = tween(animationDuration))
                }
            }
        }
    }
    
    Button(
        onClick = onClick,
        modifier = modifier.scale(scale.value),
        enabled = enabled,
        shape = shape,
        colors = buttonColors,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        if (icon != null && iconPosition == ButtonIconPosition.START) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Text(
            text = text,
            style = if (isPrimary) MaterialTheme.typography.extended.buttonPrimary
            else MaterialTheme.typography.extended.buttonSecondary,
            fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.SemiBold
        )
        
        if (icon != null && iconPosition == ButtonIconPosition.END) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

enum class ButtonIconPosition {
    START, END
}

/**
 * Animated icon button with hover effects
 */
@Composable
fun AnimatedIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector,
    contentDescription: String? = null,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    size: Dp = 24.dp,
    animationDuration: Int = 150
) {
    val scale = remember { Animatable(1f) }
    val rotation = remember { Animatable(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is androidx.compose.foundation.interaction.PressInteraction.Press -> {
                    scale.animateTo(0.9f, animationSpec = tween(animationDuration))
                    rotation.animateTo(5f, animationSpec = tween(animationDuration))
                }
                is androidx.compose.foundation.interaction.PressInteraction.Release -> {
                    scale.animateTo(1f, animationSpec = tween(animationDuration))
                    rotation.animateTo(0f, animationSpec = tween(animationDuration))
                }
                is androidx.compose.foundation.interaction.PressInteraction.Cancel -> {
                    scale.animateTo(1f, animationSpec = tween(animationDuration))
                    rotation.animateTo(0f, animationSpec = tween(animationDuration))
                }
            }
        }
    }
    
    IconButton(
        onClick = onClick,
        modifier = modifier.scale(scale.value),
        enabled = enabled,
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(size)
        )
    }
}

/**
 * Enhanced switch with custom styling
 */
@Composable
fun AnimatedSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = MaterialTheme.colorScheme.primary,
        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
    )
) {
    val scale = remember { Animatable(1f) }
    
    LaunchedEffect(checked) {
        scale.animateTo(1.1f, animationSpec = tween(100))
        scale.animateTo(1f, animationSpec = tween(100))
    }
    
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.scale(scale.value),
        enabled = enabled,
        colors = colors
    )
}

/**
 * Animated section header with expand/collapse functionality
 */
@Composable
fun AnimatedSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isExpanded: Boolean = true,
    onExpandChange: ((Boolean) -> Unit)? = null,
    subtitle: String? = null,
    titleColor: Color = MaterialTheme.colorScheme.primary,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "rotation"
    )
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onExpandChange != null) {
                    Modifier.clickable { onExpandChange(!isExpanded) }
                } else {
                    Modifier
                }
            )
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = titleColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Text(
                text = title,
                style = MaterialTheme.typography.extended.settingsSection,
                color = titleColor,
                modifier = Modifier.weight(1f)
            )
            
            if (onExpandChange != null) {
                Icon(
                    imageVector = GameIcons.UI.expand,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = titleColor,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(rotation)
                )
            }
        }
        
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.extended.settingsDescription,
                color = subtitleColor,
                modifier = Modifier.padding(
                    start = if (icon != null) 32.dp else 0.dp,
                    top = 4.dp
                )
            )
        }
    }
}

/**
 * Animated gradient background component
 */
@Composable
fun AnimatedGradientBackground(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        MaterialTheme.extendedColors.gradientStart,
        MaterialTheme.extendedColors.gradientMid,
        MaterialTheme.extendedColors.gradientEnd
    ),
    animationDuration: Int = 3000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient_animation")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = androidx.compose.ui.geometry.Offset(0f, offset * 1000f),
                    end = androidx.compose.ui.geometry.Offset(1000f, (1f - offset) * 1000f)
                )
            )
    )
}

/**
 * Achievement badge component with unlock animation
 */
@Composable
fun AchievementBadge(
    rank: Int,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    isUnlocked: Boolean = true,
    showUnlockAnimation: Boolean = false
) {
    val scale = remember { Animatable(1f) }
    val rotation = remember { Animatable(0f) }
    val alpha = remember { Animatable(if (isUnlocked) 1f else 0.3f) }
    
    LaunchedEffect(showUnlockAnimation) {
        if (showUnlockAnimation && isUnlocked) {
            // Unlock animation sequence
            scale.animateTo(1.5f, animationSpec = tween(300))
            rotation.animateTo(360f, animationSpec = tween(500))
            scale.animateTo(1f, animationSpec = tween(200))
            rotation.animateTo(0f, animationSpec = tween(100))
        }
    }
    
    LaunchedEffect(isUnlocked) {
        alpha.animateTo(if (isUnlocked) 1f else 0.3f, animationSpec = tween(300))
    }
    
    val badgeColor = when (rank) {
        1 -> MaterialTheme.extendedColors.achievementGold
        2 -> MaterialTheme.extendedColors.achievementSilver
        3 -> MaterialTheme.extendedColors.achievementBronze
        else -> MaterialTheme.colorScheme.secondary
    }
    
    Box(
        modifier = modifier
            .size(size)
            .scale(scale.value)
            .alpha(alpha.value)
            .clip(MaterialTheme.shapes.extended.achievementBadge)
            .background(badgeColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = getAchievementIcon(rank),
            contentDescription = "Achievement badge",
            tint = Color.White,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

/**
 * Pulsing indicator for loading states
 */
@Composable
fun PulsingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 24.dp,
    animationDuration: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .alpha(alpha)
            .background(color, shape = MaterialTheme.shapes.medium)
    )
}

/**
 * Animated counter for scores and statistics
 */
@Composable
fun AnimatedCounter(
    targetValue: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    color: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = 1000,
    prefix: String = "",
    suffix: String = ""
) {
    var animatedValue by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(targetValue) {
        val animationSteps = 50
        val stepDelay = animationDuration / animationSteps
        val stepSize = (targetValue - animatedValue) / animationSteps
        
        repeat(animationSteps) {
            delay(stepDelay.toLong())
            animatedValue += stepSize
        }
        animatedValue = targetValue
    }
    
    Text(
        text = "$prefix$animatedValue$suffix",
        style = textStyle,
        color = color,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}