package com.xbot.sharedapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveComponentOverrideApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.xbot.navigation.Navigator
import com.xbot.navigation.TopLevelRoutes
import org.jetbrains.compose.resources.stringResource

internal object ComposeNavigationChrome : NavigationChrome {

    @OptIn(
        ExperimentalMaterial3ExpressiveApi::class,
        ExperimentalMaterial3AdaptiveComponentOverrideApi::class,
    )
    @Composable
    override fun Content(navigator: Navigator, content: @Composable () -> Unit) {
        val scaffoldState = rememberNavigationSuiteScaffoldState()
        val navSuiteType =
            NavigationSuiteScaffoldDefaults.navigationSuiteType(currentWindowAdaptiveInfoV2())

        val currentTopLevelDestination = navigator.currentTopLevelDestination
        val chromeHidden = navigator.currentDestination?.hidesNavigationBar == true

        LaunchedEffect(scaffoldState, chromeHidden) {
            if (chromeHidden) scaffoldState.hide() else scaffoldState.show()
        }

        NavigationSuiteScaffold(
            navigationItems = {
                TopLevelRoutes.forEach { destination ->
                    val isSelected = currentTopLevelDestination == destination

                    NavigationSuiteItem(
                        selected = isSelected,
                        onClick = { navigator.navigate(destination) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                contentDescription = stringResource(destination.textRes),
                            )
                        },
                        label = { Text(stringResource(destination.textRes)) },
                        navigationSuiteType = navSuiteType,
                    )
                }
            },
            navigationSuiteType = navSuiteType,
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                shortNavigationBarContainerColor = MaterialTheme.colorScheme.surface,
                navigationBarContainerColor = MaterialTheme.colorScheme.surface,
            ),
            state = scaffoldState,
            navigationItemVerticalArrangement = Arrangement.Center,
            content = content,
        )
    }
}
