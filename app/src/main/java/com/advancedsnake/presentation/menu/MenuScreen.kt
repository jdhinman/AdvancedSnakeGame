package com.advancedsnake.presentation.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.advancedsnake.R

@Composable
fun MenuScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(64.dp))
        Button(onClick = { navController.navigate("game") }) {
            Text(stringResource(id = R.string.play))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO: Implement Leaderboard */ }) {
            Text(stringResource(id = R.string.leaderboard))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO: Implement Settings */ }) {
            Text(stringResource(id = R.string.settings))
        }
    }
}