package com.xbot.anilibriarefresh.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    destinations: List<NavigationItem> = NavigationItem.destinations,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    androidx.compose.material3.NavigationBar (
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        windowInsets = windowInsets
    ) {
        destinations.forEach { destination ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(destination.route::class)
            } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.title
                    )
                },
                label = {
                    Text(
                        text = destination.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}

sealed class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: Screen
) {
    data object Home : NavigationItem(
        title = "Home",
        icon = Icons.Rounded.Home,
        route = Screen.Home
    )
    data object Favorite : NavigationItem(
        title = "Favorite",
        icon = Icons.Rounded.Favorite,
        route = Screen.Favorite
    )
    data object Profile : NavigationItem(
        title = "Profile",
        icon = Icons.Rounded.Person,
        route = Screen.Profile
    )

    companion object {
        val destinations = listOf(Home, Favorite, Profile)
    }
}
