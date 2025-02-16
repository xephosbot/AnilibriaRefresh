package com.xbot.anilibriarefresh.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.xbot.anilibriarefresh.navigation.TopLevelDestination
import com.xbot.common.navigation.currentBackStackAsState
import com.xbot.common.navigation.hasRoute

@Composable
fun rememberAnilibriaAppState(
    navController: NavHostController = rememberNavController(),
): AnilibriaAppState {
    return remember(navController) {
        AnilibriaAppState(navController)
    }
}

@Stable
class AnilibriaAppState(
    val navController: NavHostController,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryAsState()

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination.hasRoute(route = topLevelDestination.route)
            }
        }

    val navBackStack: List<NavBackStackEntry>?
        @Composable get() {
            return navController.currentBackStackAsState().value
        }

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(
        topLevelDestination: TopLevelDestination,
        navBackStack: List<NavBackStackEntry>? = null
    ) {
        val firstTopLevelDestination = navBackStack
            ?.firstOrNull { TopLevelDestination.classNames.contains(it.destination.route) }
            ?.destination
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            if (firstTopLevelDestination != null) {
                popUpTo(firstTopLevelDestination.id) {
                    saveState = true
                }
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        navController.navigate(topLevelDestination.route, topLevelNavOptions)
    }
}