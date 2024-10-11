package com.xbot.anilibriarefresh.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xbot.anilibriarefresh.ui.feature.favorite.FavoriteScreen
import com.xbot.anilibriarefresh.ui.feature.home.HomeScreen
import com.xbot.anilibriarefresh.ui.feature.title.TitleScreen
import kotlinx.serialization.Serializable

@Composable
fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    startDestination: Any = HomeScreen
) {
    NavHost(
        modifier = modifier
            .fillMaxSize()
            .background(containerColor),
        navController = navController,
        startDestination = startDestination
    ) {
        composable<HomeScreen> {
            HomeScreen()
        }
        composable<TitleScreen> {
            TitleScreen()
        }
        composable<FavoriteScreen> {
            FavoriteScreen()
        }
    }
}

@Serializable
object HomeScreen

@Serializable
data class TitleScreen(val titleId: Int)

@Serializable
object FavoriteScreen