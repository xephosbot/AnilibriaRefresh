package com.xbot.anilibriarefresh.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.xbot.anilibriarefresh.ui.PlayerActivity
import com.xbot.anilibriarefresh.ui.feature.favorite.FavoriteScreen
import com.xbot.anilibriarefresh.ui.feature.home.navigation.homeSection
import com.xbot.anilibriarefresh.ui.feature.title.TitleScreen
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@Composable
fun AnilibriaNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Route = Route.Home,
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination,
        enterTransition = { materialFadeThroughIn() },
        exitTransition = { materialFadeThroughOut() },
    ) {
        homeSection { releaseId ->
            navController.navigate(Route.Detail(releaseId))
        }
        composable<Route.Favorite> {
            FavoriteScreen()
        }
        composable<Route.Profile> {
            FavoriteScreen()
        }
        composable<Route.Detail> {
            TitleScreen()
        }
        activity<Route.Player> {
            this.activityClass = PlayerActivity::class
        }
    }
}

// https://issuetracker.google.com/issues/360354551
@SuppressLint("RestrictedApi")
fun NavDestination?.hasRoute(route: Route) =
    this?.hierarchy?.any { it.hasRoute(route::class) } == true

@Composable
fun NavController.isTopLevelDestination(): Boolean {
    currentBackStackEntryAsState().value
    return previousBackStackEntry == null
}

/**
 * Это временное решение, пока не будет доступен публичный API.
 * Используется внутренний API Jetpack Compose Navigations, отсюда и подавление предупреждений.
 *
 * @return состояние, представляющее currentBackStack.
 */
@SuppressLint("RestrictedApi")
@Composable
fun NavController.currentBackStackAsState(): State<List<NavBackStackEntry>?> {
    return currentBackStack.collectAsState(null)
}

/**
 * Это обходной путь для устранения дублирования событий навигации.
 * @see <a href="https://github.com/android/compose-samples/blob/081721ad44dfb29b55b1bc34f83d693b6b8dc9dd/Jetsnack/app/src/main/java/com/example/jetsnack/ui/JetsnackAppState.kt#L141-L147">Jetsnack Sample</a>
 */
internal fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
