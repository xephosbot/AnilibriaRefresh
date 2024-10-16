package com.xbot.anilibriarefresh.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.xbot.anilibriarefresh.ui.feature.favorite.FavoriteScreen
import com.xbot.anilibriarefresh.ui.feature.home.HomeScreen
import com.xbot.anilibriarefresh.ui.feature.title.TitleScreen

@Composable
fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    startDestination: Route = Route.Home
) {
    NavHost(
        modifier = modifier
            .fillMaxSize()
            .background(containerColor),
        navController = navController,
        startDestination = startDestination
    ) {
        navigation<Route.Home>(
            startDestination = Route.Home.List
        ) {
            composable<Route.Home.List> { backStackEntry ->
                HomeScreen(
                    paddingValues = paddingValues
                ) { titleId ->
                    if (backStackEntry.lifecycleIsResumed()) {
                        navController.navigate(Route.Home.Detail(titleId))
                    }
                }
            }
            composable<Route.Home.Detail> {
                TitleScreen()
            }
        }
        composable<Route.Favorite> {
            FavoriteScreen()
        }
        composable<Route.Search> {
            FavoriteScreen()
        }
        composable<Route.Profile> {
            FavoriteScreen()
        }
    }
}

fun NavDestination?.hasRoute(destination: TopLevelDestination) =
    this?.hierarchy?.any { it.hasRoute(destination.route::class) } == true

/**
 * Это обходной путь для устранения дублирования событий навигации.
 * @see <a href="https://github.com/android/compose-samples/blob/081721ad44dfb29b55b1bc34f83d693b6b8dc9dd/Jetsnack/app/src/main/java/com/example/jetsnack/ui/JetsnackAppState.kt#L141-L147">Jetsnack Sample</a>
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
