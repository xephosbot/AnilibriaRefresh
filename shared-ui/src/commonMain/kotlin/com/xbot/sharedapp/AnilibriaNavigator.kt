package com.xbot.sharedapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.TopLevelNavKey
import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.home.navigation.HomeRoute
import com.xbot.profile.navigation.ProfileRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
internal fun rememberAnilibriaNavigator(
    navController: NavHostController = rememberNavController(),
): Navigator<NavBackStackEntry> {
    val scope = rememberCoroutineScope()
    return remember(navController, scope) { AnilibriaNavigator(navController, scope) }
}

internal class AnilibriaNavigator(
    val navController: NavHostController,
    scope: CoroutineScope
) : Navigator<NavBackStackEntry> {

    init {
        navController.currentBackStack
            .onEach { entries ->
                _backstack = entries
            }.launchIn(scope)

        navController.currentBackStackEntryFlow
            .onEach { entry ->
                _currentDestination = entry
            }.launchIn(scope)
    }

    private var _backstack: List<NavBackStackEntry> by mutableStateOf(emptyList())
    override val backstack: List<NavBackStackEntry>
        get() = _backstack

    private var _currentDestination: NavBackStackEntry? by mutableStateOf(null)
    override val currentDestination: NavBackStackEntry?
        get() = _currentDestination

    override val currentTopLevelDestination: NavBackStackEntry?
        get() = backstack.findLast { entry ->
            topLevelDestinations.map { it::class }.any { entry.destination.hasRoute(it) }
        }

    override fun navigate(destination: Any) {
        navController.navigate(destination)
    }

    override fun navigateTopLevel(destination: Any) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().route.orEmpty()) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navController.navigate(destination, topLevelNavOptions)
    }

    override fun navigateBack() {
        navController.navigateUp()
    }

    companion object {
        val topLevelDestinations: List<TopLevelNavKey> = listOf(HomeRoute, FavoriteRoute, ProfileRoute)
    }
}