package com.advancedsnake.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.advancedsnake.data.local.dao.ScoreDao
import com.advancedsnake.data.local.entities.ScoreEntity

@Database(
    entities = [ScoreEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun scoreDao(): ScoreDao
    
    companion object {
        const val DATABASE_NAME = "snake_game_database"
    }
}