package com.xbot.navigation.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import androidx.window.core.layout.WindowSizeClass
import com.xbot.navigation.LocalNavigator
import com.xbot.navigation.TopLevelNavKey
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T : Any> rememberNavigationSuiteSceneDecoratorStrategy(
    topLevelRoutes: Set<TopLevelNavKey>,
    navigationSuiteType: @Composable () -> NavigationSuiteType = {
        NavigationSuiteSceneDecoratorDefaults.navigationSuiteType(currentWindowAdaptiveInfoV2())
    },
    state: NavigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState(),
    colors: NavigationSuiteColors = NavigationSuiteDefaults.colors(
        shortNavigationBarContainerColor = MaterialTheme.colorScheme.surface,
        navigationBarContainerColor = MaterialTheme.colorScheme.surface,
    ),
    itemVerticalArrangement: Arrangement.Vertical = Arrangement.Center,
): NavigationSuiteSceneDecoratorStrategy<T> =
    remember(topLevelRoutes, navigationSuiteType, state, colors, itemVerticalArrangement) {
        NavigationSuiteSceneDecoratorStrategy(
            topLevelRoutes = topLevelRoutes,
            navigationSuiteType = navigationSuiteType,
            state = state,
            colors = colors,
            itemVerticalArrangement = itemVerticalArrangement,
        )
    }

/**
 * Wraps every [Scene]'s content in a [NavigationSuiteScaffold] that renders an item per entry of
 * [topLevelRoutes]. The current top-level destination and click handling are read from
 * [LocalNavigator], so callers don't need to thread the navigator through the API.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
class NavigationSuiteSceneDecoratorStrategy<T : Any>(
    private val topLevelRoutes: Set<TopLevelNavKey>,
    private val navigationSuiteType: @Composable () -> NavigationSuiteType,
    private val state: NavigationSuiteScaffoldState,
    private val colors: NavigationSuiteColors,
    private val itemVerticalArrangement: Arrangement.Vertical,
) : SceneDecoratorStrategy<T> {
    override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> =
        NavigationSuiteDecoratingScene(
            scene = scene,
            topLevelRoutes = topLevelRoutes,
            navigationSuiteType = navigationSuiteType,
            state = state,
            colors = colors,
            itemVerticalArrangement = itemVerticalArrangement,
        )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private class NavigationSuiteDecoratingScene<T : Any>(
    private val scene: Scene<T>,
    private val topLevelRoutes: Set<TopLevelNavKey>,
    private val navigationSuiteType: @Composable () -> NavigationSuiteType,
    private val state: NavigationSuiteScaffoldState,
    private val colors: NavigationSuiteColors,
    private val itemVerticalArrangement: Arrangement.Vertical,
) : Scene<T> {
    override val key: Any = scene::class to scene.key
    override val entries: List<NavEntry<T>> = scene.entries
    override val previousEntries: List<NavEntry<T>> = scene.previousEntries
    override val metadata: Map<String, Any> = scene.metadata
    override val content: @Composable () -> Unit = {
        val navigator = LocalNavigator.current
        val currentTopLevelDestination = navigator.currentTopLevelDestination
        val navSuiteType = navigationSuiteType()

        NavigationSuiteScaffold(
            navigationItems = {
                topLevelRoutes.forEach { destination ->
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
            navigationSuiteColors = colors,
            state = state,
            navigationItemVerticalArrangement = itemVerticalArrangement,
        ) {
            scene.content()
        }
    }
}

/**
 * Defaults for [rememberNavigationSuiteSceneDecoratorStrategy].
 */
object NavigationSuiteSceneDecoratorDefaults {
    /**
     * Picks a [NavigationSuiteType] from the given [adaptiveInfo]. Mirrors the previous
     * design-system mapping: a short bar on compact/medium phones (with a tabletop or short-height
     * carve-out), a collapsed rail on medium-width tablets, and an expanded rail otherwise.
     */
    fun navigationSuiteType(adaptiveInfo: WindowAdaptiveInfo): NavigationSuiteType =
        with(adaptiveInfo) {
            when {
                !windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) ->
                    NavigationSuiteType.ShortNavigationBarCompact

                windowPosture.isTabletop ||
                        !windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
                    -> NavigationSuiteType.ShortNavigationBarMedium

                windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) &&
                        !windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
                    -> NavigationSuiteType.WideNavigationRailCollapsed

                else -> NavigationSuiteType.WideNavigationRailExpanded
            }
        }
}
