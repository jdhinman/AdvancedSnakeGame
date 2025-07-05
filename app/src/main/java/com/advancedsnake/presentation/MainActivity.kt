package com.advancedsnake.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.advancedsnake.presentation.game.GameScreen
import com.advancedsnake.presentation.menu.MenuScreen
import com.advancedsnake.presentation.theme.AdvancedSnakeGameTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdvancedSnakeGameTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "menu") {
                    composable("menu") {
                        MenuScreen(navController)
                    }
                    composable("game") {
                        GameScreen(navController)
                    }
                }
            }
        }
    }
}