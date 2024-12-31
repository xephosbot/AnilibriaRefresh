package com.xbot.anilibriarefresh.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.xbot.anilibriarefresh.navigation.TopLevelDestination
import com.xbot.anilibriarefresh.navigation.currentBackStackAsState
import com.xbot.anilibriarefresh.navigation.hasRoute
import com.xbot.designsystem.components.NavigationBar

@Composable
fun AnilibriaNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navBackStack by navController.currentBackStackAsState()

    NavigationBar(
        modifier = modifier,
    ) {
        TopLevelDestination.entries.forEach { destination ->
            val isSelected = currentDestination.hasRoute(destination.route)
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    val firstTopLevelDestination = navBackStack
                        ?.firstOrNull { TopLevelDestination.classNames.contains(it.destination.route) }
                        ?.destination

                    navController.navigate(destination.route) {
                        if (firstTopLevelDestination != null) {
                            popUpTo(firstTopLevelDestination.id) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = when (isSelected) {
                            true -> destination.selectedIcon
                            else -> destination.unselectedIcon
                        },
                        contentDescription = stringResource(destination.textResId),
                    )
                }
            )
        }
    }
}
