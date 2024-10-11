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
import androidx.navigation.compose.navigation
import com.xbot.anilibriarefresh.ui.feature.favorite.FavoriteScreen
import com.xbot.anilibriarefresh.ui.feature.home.HomeScreen
import com.xbot.anilibriarefresh.ui.feature.title.TitleScreen
import kotlinx.serialization.Serializable

@Composable
fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    startDestination: Screen = Screen.Home
) {
    NavHost(
        modifier = modifier
            .fillMaxSize()
            .background(containerColor),
        navController = navController,
        startDestination = startDestination
    ) {
        navigation<Screen.Home>(
            startDestination = Screen.Home.List
        ) {
            composable<Screen.Home.List> {
                HomeScreen { titleId ->
                    navController.navigate(Screen.Home.Detail(titleId))
                }
            }
            composable<Screen.Home.Detail> {
                TitleScreen()
            }
        }
        composable<Screen.Favorite> {
            FavoriteScreen()
        }
        composable<Screen.Profile> {
            FavoriteScreen()
        }
    }
}

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen() {
        @Serializable
        data object List

        @Serializable
        data class Detail(val titleId: Int)
    }

    @Serializable
    data object Favorite : Screen()

    @Serializable
    data object Profile : Screen()
}
