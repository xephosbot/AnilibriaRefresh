package com.xbot.anilibriarefresh.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    destinations: List<NavigationItem<out Any>> = NavigationItem.destinations,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    androidx.compose.material3.NavigationBar (
        modifier = modifier
    ) {
        destinations.forEach { destination ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == destination.route::class.qualifiedName
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

sealed class NavigationItem<T>(
    val title: String,
    val icon: ImageVector,
    val route: T
) {
    data object Home : NavigationItem<HomeScreen>(
        title = "Home",
        icon = Icons.Rounded.Home,
        route = HomeScreen
    )
    data object Favorite : NavigationItem<FavoriteScreen>(
        title = "Favorite",
        icon = Icons.Rounded.Favorite,
        route = FavoriteScreen
    )
    data object Profile : NavigationItem<TitleScreen>(
        title = "Profile",
        icon = Icons.Rounded.Person,
        route = TitleScreen(9000)
    )

    companion object {
        val destinations = listOf(Home, Favorite, Profile)
    }
}
