package com.xbot.sharedapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.SingletonImageLoader
import com.xbot.common.navigation.LocalNavigator
import com.xbot.designsystem.components.NavigationSuiteScaffoldDefaults
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.Search
import com.xbot.designsystem.theme.AnilibriaTheme
import com.xbot.domain.models.enums.ThemeOption
import com.xbot.home.navigation.HomeRoute
import com.xbot.resources.Res
import com.xbot.resources.fab_search
import com.xbot.search.navigation.navigateToSearch
import com.xbot.sharedapp.navigation.AnilibriaNavGraph
import com.xbot.sharedapp.navigation.TopLevelRoutes
import com.xbot.sharedapp.navigation.rememberNavigator
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AnilibriaApp(
    viewModel: AppViewModel = koinViewModel()
) {
    SingletonImageLoader.setSafe(koinInject())

    val appearanceSettings by viewModel.appearanceSettings.collectAsStateWithLifecycle()

    val navigator = rememberNavigator(
        startRoute = HomeRoute,
        topLevelRoutes = TopLevelRoutes,
        serializersModule = koinInject()
    )

    val darkTheme = when (appearanceSettings.themeOption) {
        ThemeOption.System -> isSystemInDarkTheme()
        ThemeOption.Dark -> true
        ThemeOption.Light -> false
    }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        AnilibriaTheme(
            darkTheme = darkTheme,
            dynamicColor = appearanceSettings.isDynamicTheme,
            amoled = appearanceSettings.isPureBlack,
            expressiveColor = appearanceSettings.isExpressiveColor
        ) {
            val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
            val navSuiteType =
                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())

            val currentDestination = navigator.currentDestination
            val currentTopLevelDestination = navigator.currentTopLevelDestination

            val fabModifier = if (
                navSuiteType == NavigationSuiteType.ShortNavigationBarMedium ||
                navSuiteType == NavigationSuiteType.ShortNavigationBarCompact
            ) {
                Modifier.animateFloatingActionButton(
                    visible = currentDestination == currentTopLevelDestination,
                    alignment = Alignment.Center
                )
            } else {
                Modifier
            }
            
            val fab = @Composable {
                SearchFloatingActionButton(
                    expanded = navSuiteType == NavigationSuiteType.WideNavigationRailExpanded,
                    showOnlyIcon = navSuiteType == NavigationSuiteType.ShortNavigationBarMedium,
                    modifier = fabModifier,
                    onClick = { navigator.navigateToSearch() }
                )
            }
            val fabMovable = remember(fab) { movableContentOf(fab) }

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
                primaryActionContent = fabMovable,
                navigationSuiteType = navSuiteType,
                navigationSuiteColors = NavigationSuiteDefaults.colors(
                    shortNavigationBarContainerColor = MaterialTheme.colorScheme.surface,
                    navigationBarContainerColor = MaterialTheme.colorScheme.surface,
                ),
                state = navigationSuiteScaffoldState,
                navigationItemVerticalArrangement = Arrangement.Center
            ) {
                AnilibriaNavGraph(navigator = navigator)
            }
        }
    }
}

@Composable
private fun SearchFloatingActionButton(
    expanded: Boolean,
    showOnlyIcon: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val startPadding = if (showOnlyIcon) 0.dp else 20.dp
    ExtendedFloatingActionButton(
        modifier = Modifier
            .padding(start = startPadding)
            .then(modifier),
        onClick = onClick,
        expanded = expanded,
        icon = {
            Icon(
                imageVector = AnilibriaIcons.Search,
                contentDescription = null
            )
        },
        text = { Text(stringResource(Res.string.fab_search)) }
    )
}
