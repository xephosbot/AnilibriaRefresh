package com.xbot.shared.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.xbot.shared.ui.navigation.AnilibriaNavGraph
import com.xbot.shared.ui.navigation.hasRoute
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnilibriaApp(
    appState: AnilibriaAppState = rememberAnilibriaAppState()
) {
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
    val currentDestination = appState.currentDestination
    val currentTopLevelDestination = appState.currentTopLevelDestination

    LaunchedEffect(currentTopLevelDestination) {
        snapshotFlow { currentTopLevelDestination }
            .onEach {
                if (it != null) {
                    navigationSuiteScaffoldState.show()
                } else {
                    navigationSuiteScaffoldState.hide()
                }
            }
            .collect()
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            appState.topLevelDestinations.forEach { destination ->
                val isSelected = currentDestination.hasRoute(destination.route)
                item(
                    selected = isSelected,
                    icon = {
                        Icon(
                            imageVector = when (isSelected) {
                                true -> destination.selectedIcon
                                else -> destination.unselectedIcon
                            },
                            contentDescription = stringResource(destination.textRes),
                        )
                    },
                    onClick = {
                        appState.navigateToTopLevelDestination(destination)
                    },
                )
            }
        },
        layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
        state = navigationSuiteScaffoldState
    ) {
        AnilibriaNavGraph(
            appState = appState
        )
    }
}
