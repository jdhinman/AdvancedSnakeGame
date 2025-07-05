package com.advancedsnake.domain.entities

import java.text.SimpleDateFormat
import java.util.*

data class Score(
    val id: Long = 0,
    val score: Int,
    val playerName: String,
    val snakeLength: Int,
    val gameSpeedLevel: String,
    val timestamp: Long,
    val gameDurationMs: Long
) {
    val formattedDate: String
        get() {
            val date = Date(timestamp)
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return formatter.format(date)
        }
    
    val formattedTime: String
        get() {
            val date = Date(timestamp)
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formatter.format(date)
        }
    
    val formattedDuration: String
        get() {
            val seconds = gameDurationMs / 1000
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return if (minutes > 0) {
                "${minutes}m ${remainingSeconds}s"
            } else {
                "${remainingSeconds}s"
            }
        }
    
    val relativeTime: String
        get() {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < 60_000 -> "Just now"
                diff < 3_600_000 -> "${diff / 60_000}m ago"
                diff < 86_400_000 -> "${diff / 3_600_000}h ago"
                diff < 604_800_000 -> "${diff / 86_400_000}d ago"
                else -> formattedDate
            }
        }
}