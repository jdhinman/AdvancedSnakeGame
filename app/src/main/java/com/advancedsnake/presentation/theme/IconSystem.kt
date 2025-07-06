package com.advancedsnake.presentation.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Comprehensive icon system for the Advanced Snake Game
 * Provides consistent iconography across all screens
 */
object GameIcons {
    
    // Game navigation icons
    val play = Icons.Default.PlayArrow
    val pause = Icons.Default.Pause
    val stop = Icons.Default.Stop
    val restart = Icons.Default.Refresh
    val home = Icons.Default.Home
    val back = Icons.Default.ArrowBack
    val close = Icons.Default.Close
    
    // Settings category icons
    object Settings {
        val gamepad = Icons.Default.SportsEsports
        val audio = Icons.Default.VolumeUp
        val audioOff = Icons.Default.VolumeOff
        val visual = Icons.Default.Palette
        val display = Icons.Default.PhoneAndroid
        val vibration = Icons.Default.Vibration
        val player = Icons.Default.Person
        val general = Icons.Default.Settings
        val reset = Icons.Default.RestoreFromTrash
    }
    
    // Leaderboard and achievement icons
    object Leaderboard {
        val trophy = Icons.Default.EmojiEvents
        val star = Icons.Default.Star
        val starOutline = Icons.Outlined.Star
        val medal = Icons.Default.WorkspacePremium
        val crown = Icons.Default.Grade
        val rank1 = Icons.Default.LooksOne
        val rank2 = Icons.Default.LooksTwo
        val rank3 = Icons.Default.Looks3
        val statistics = Icons.Default.BarChart
        val trend = Icons.Default.TrendingUp
        val delete = Icons.Default.Delete
        val clear = Icons.Default.ClearAll
    }
    
    // Achievement specific icons
    object Achievement {
        val gold = Icons.Default.EmojiEvents
        val silver = Icons.Default.WorkspacePremium
        val bronze = Icons.Default.Grade
        val unlock = Icons.Default.LockOpen
        val lock = Icons.Default.Lock
        val progress = Icons.Default.Timeline
        val milestone = Icons.Default.Flag
        val celebration = Icons.Default.Celebration
        val badge = Icons.Default.Shield
        val streak = Icons.Default.Whatshot
    }
    
    // Game state icons
    object GameState {
        val success = Icons.Default.CheckCircle
        val warning = Icons.Default.Warning
        val error = Icons.Default.Error
        val info = Icons.Default.Info
        val loading = Icons.Default.Autorenew
        val empty = Icons.Default.HourglassEmpty
    }
    
    // UI control icons
    object UI {
        val expand = Icons.Default.ExpandMore
        val collapse = Icons.Default.ExpandLess
        val next = Icons.Default.ChevronRight
        val previous = Icons.Default.ChevronLeft
        val up = Icons.Default.KeyboardArrowUp
        val down = Icons.Default.KeyboardArrowDown
        val check = Icons.Default.Check
        val add = Icons.Default.Add
        val remove = Icons.Default.Remove
        val edit = Icons.Default.Edit
        val save = Icons.Default.Save
        val cancel = Icons.Default.Cancel
        val visibility = Icons.Default.Visibility
        val visibilityOff = Icons.Default.VisibilityOff
    }
    
    // Snake game specific icons
    object Snake {
        val snake = Icons.Default.Timeline // Using timeline as snake representation
        val food = Icons.Default.Circle
        val grid = Icons.Default.GridView
        val speed = Icons.Default.Speed
        val length = Icons.Default.StraightenOutlined
        val direction = Icons.Default.Navigation
        val score = Icons.Default.ScoreboardOutlined
        val level = Icons.Default.Stairs
        val board = Icons.Default.GridOn
        val theme = Icons.Default.ColorLens
    }
    
    // Status indicators
    object Status {
        val active = Icons.Default.RadioButtonChecked
        val inactive = Icons.Default.RadioButtonUnchecked
        val online = Icons.Default.CloudDone
        val offline = Icons.Default.CloudOff
        val pending = Icons.Default.HourglassTop
        val complete = Icons.Default.TaskAlt
        val failed = Icons.Default.ErrorOutline
    }
    
    // Social and sharing icons
    object Social {
        val share = Icons.Default.Share
        val favorite = Icons.Default.Favorite
        val favoriteOutline = Icons.Outlined.FavoriteBorder
        val bookmark = Icons.Default.Bookmark
        val bookmarkOutline = Icons.Outlined.BookmarkBorder
        val comment = Icons.Default.Comment
        val like = Icons.Default.ThumbUp
        val dislike = Icons.Default.ThumbDown
    }
    
    // Utility icons
    object Utility {
        val help = Icons.Default.Help
        val about = Icons.Default.Info
        val feedback = Icons.Default.Feedback
        val bug = Icons.Default.BugReport
        val feature = Icons.Default.NewReleases
        val update = Icons.Default.SystemUpdateAlt
        val download = Icons.Default.Download
        val upload = Icons.Default.Upload
        val sync = Icons.Default.Sync
        val search = Icons.Default.Search
        val filter = Icons.Default.FilterList
        val sort = Icons.Default.Sort
    }
}

/**
 * Helper function to get achievement icon based on rank
 */
fun getAchievementIcon(rank: Int): ImageVector {
    return when (rank) {
        1 -> GameIcons.Achievement.gold
        2 -> GameIcons.Achievement.silver
        3 -> GameIcons.Achievement.bronze
        else -> GameIcons.Achievement.badge
    }
}

/**
 * Helper function to get rank icon based on position
 */
fun getRankIcon(rank: Int): ImageVector {
    return when (rank) {
        1 -> GameIcons.Leaderboard.rank1
        2 -> GameIcons.Leaderboard.rank2
        3 -> GameIcons.Leaderboard.rank3
        else -> GameIcons.Leaderboard.star
    }
}

/**
 * Helper function to get status icon based on state
 */
fun getStatusIcon(isActive: Boolean): ImageVector {
    return if (isActive) GameIcons.Status.active else GameIcons.Status.inactive
}

/**
 * Helper function to get game state icon
 */
fun getGameStateIcon(state: String): ImageVector {
    return when (state.lowercase()) {
        "success", "complete", "win" -> GameIcons.GameState.success
        "warning", "caution" -> GameIcons.GameState.warning
        "error", "fail", "loss" -> GameIcons.GameState.error
        "info", "information" -> GameIcons.GameState.info
        "loading", "processing" -> GameIcons.GameState.loading
        "empty", "none" -> GameIcons.GameState.empty
        else -> GameIcons.GameState.info
    }
}

/**
 * Helper function to get audio icon based on state
 */
fun getAudioIcon(isEnabled: Boolean): ImageVector {
    return if (isEnabled) GameIcons.Settings.audio else GameIcons.Settings.audioOff
}

/**
 * Helper function to get visibility icon based on state
 */
fun getVisibilityIcon(isVisible: Boolean): ImageVector {
    return if (isVisible) GameIcons.UI.visibility else GameIcons.UI.visibilityOff
}