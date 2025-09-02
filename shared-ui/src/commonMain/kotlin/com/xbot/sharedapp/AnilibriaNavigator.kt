package com.xbot.sharedapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.xbot.common.navigation.Navigator
import com.xbot.sharedapp.navigation.TopLevelDestination
import com.xbot.sharedapp.navigation.currentBackStackAsState

@Composable
internal fun rememberAnilibriaNavigator(
    navController: NavHostController = rememberNavController(),
): AnilibriaNavigator {
    return remember(navController) { AnilibriaNavigator(navController) }
}

internal class AnilibriaNavigator(
    val navController: NavHostController
) : Navigator<NavBackStackEntry> {

    override val backstack: List<NavBackStackEntry?>
        @Composable
        get() = navController.currentBackStackAsState().value

    override val currentDestination: NavBackStackEntry?
        @Composable
        get() = navController.currentBackStackEntryAsState().value

    override val currentTopLevelDestination: NavBackStackEntry?
        @Composable
        get() = backstack.findLast { entry ->
            topLevelDestinations.map { it.route::class }.any { entry?.destination?.hasRoute(it) == true }
        }

    override fun navigate(destination: Any, singleTop: Boolean) {
        if (singleTop) {
            navigateToTopLevelDestination(destination)
        } else {
            navController.navigate(destination)
        }
    }

    private fun navigateToTopLevelDestination(destination: Any) {
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
        val topLevelDestinations = TopLevelDestination.entries
    }
}