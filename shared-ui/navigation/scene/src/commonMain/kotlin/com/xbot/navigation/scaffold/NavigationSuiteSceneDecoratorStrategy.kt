package com.xbot.navigation.scaffold

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.ShortNavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.window.core.layout.WindowSizeClass
import com.xbot.navigation.LocalNavigator
import com.xbot.navigation.TopLevelNavKey
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T : Any> rememberNavigationSuiteSceneDecoratorStrategy(
    topLevelRoutes: Set<TopLevelNavKey>,
    navigationSuiteType: NavigationSuiteType = NavigationSuiteSceneDecoratorDefaults
        .navigationSuiteType(currentWindowAdaptiveInfoV2()),
    state: NavigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState(),
    containerColor: Color = NavigationSuiteScaffoldDefaults.containerColor,
    contentColor: Color = NavigationSuiteScaffoldDefaults.contentColor,
    navigationSuiteColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(
        shortNavigationBarContainerColor = MaterialTheme.colorScheme.surface,
        navigationBarContainerColor = MaterialTheme.colorScheme.surface,
    ),
    itemVerticalArrangement: Arrangement.Vertical = Arrangement.Center,
): NavigationSuiteSceneDecoratorStrategy<T> {
    val currentNavigationSuiteType = rememberUpdatedState(navigationSuiteType)
    val currentContainerColor = rememberUpdatedState(containerColor)
    val currentContentColor = rememberUpdatedState(contentColor)
    val currentNavigationSuiteColors = rememberUpdatedState(navigationSuiteColors)

    return remember {
        NavigationSuiteSceneDecoratorStrategy(
            topLevelRoutes = topLevelRoutes,
            navigationSuiteType = currentNavigationSuiteType,
            state = state,
            containerColor = currentContainerColor,
            contentColor = currentContentColor,
            navigationSuiteColors = currentNavigationSuiteColors,
            itemVerticalArrangement = itemVerticalArrangement,
        )
    }
}

class NavigationSuiteSceneDecoratorStrategy<T : Any>(
    private val topLevelRoutes: Set<TopLevelNavKey>,
    private val navigationSuiteType: State<NavigationSuiteType>,
    private val state: NavigationSuiteScaffoldState,
    private val containerColor: State<Color>,
    private val contentColor: State<Color>,
    private val navigationSuiteColors: State<NavigationSuiteColors>,
    private val itemVerticalArrangement: Arrangement.Vertical,
) : SceneDecoratorStrategy<T> {
    override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> =
        NavigationSuiteDecoratingScene(
            scene = scene,
            topLevelRoutes = topLevelRoutes,
            navigationSuiteType = navigationSuiteType,
            state = state,
            containerColor = containerColor,
            contentColor = contentColor,
            navigationSuiteColors = navigationSuiteColors,
            itemVerticalArrangement = itemVerticalArrangement,
        )
}

private class NavigationSuiteDecoratingScene<T : Any>(
    private val scene: Scene<T>,
    private val topLevelRoutes: Set<TopLevelNavKey>,
    private val navigationSuiteType: State<NavigationSuiteType>,
    private val state: NavigationSuiteScaffoldState,
    private val containerColor: State<Color>,
    private val contentColor: State<Color>,
    private val navigationSuiteColors: State<NavigationSuiteColors>,
    private val itemVerticalArrangement: Arrangement.Vertical,
) : Scene<T> {
    override val key: Any = scene::class to scene.key
    override val entries: List<NavEntry<T>> = scene.entries
    override val previousEntries: List<NavEntry<T>> = scene.previousEntries
    override val metadata: Map<String, Any> = scene.metadata
    override val content: @Composable () -> Unit = {
        val animatedContentScope = LocalNavAnimatedContentScope.current
        val sharedTransitionScope = LocalNavSharedTransitionScope.current
        val isMovableContentCaller =
            animatedContentScope.transition.targetState == EnterExitState.Visible

        val navigationSuite = remember {
            movableContentOf {
                val navigator = LocalNavigator.current
                val currentTopLevelDestination = navigator.currentTopLevelDestination
                NavigationSuite(
                    navigationSuiteType = navigationSuiteType.value,
                    colors = navigationSuiteColors.value,
                    verticalArrangement = itemVerticalArrangement,
                ) {
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
                            navigationSuiteType = navigationSuiteType.value,
                        )
                    }
                }
            }
        }

        with(sharedTransitionScope) {
            Surface(color = containerColor.value, contentColor = contentColor.value) {
                NavigationSuiteScaffoldLayout(
                    navigationSuite = {
                        Box(
                            modifier = Modifier
                                .cacheSize(useCachedSize = !isMovableContentCaller)
                                .sharedElement(
                                    rememberSharedContentState("nav-suite"),
                                    animatedContentScope,
                                ),
                        ) {
                            if (isMovableContentCaller) {
                                navigationSuite()
                            }
                        }
                    },
                    navigationSuiteType = navigationSuiteType.value,
                    state = state,
                    content = {
                        Box(
                            Modifier.navigationSuiteScaffoldConsumeWindowInsets(
                                navigationSuiteType.value,
                                state,
                            )
                        ) {
                            scene.content()
                        }
                    },
                )
            }
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

@Composable
private fun Modifier.navigationSuiteScaffoldConsumeWindowInsets(
    navigationSuiteType: NavigationSuiteType,
    state: NavigationSuiteScaffoldState,
): Modifier =
    consumeWindowInsets(
        if (state.currentValue == NavigationSuiteScaffoldValue.Hidden && !state.isAnimating) {
            NoWindowInsets
        } else {
            when (navigationSuiteType) {
                NavigationSuiteType.ShortNavigationBarCompact,
                NavigationSuiteType.ShortNavigationBarMedium ->
                    ShortNavigationBarDefaults.windowInsets.only(WindowInsetsSides.Bottom)
                NavigationSuiteType.WideNavigationRailCollapsed,
                NavigationSuiteType.WideNavigationRailExpanded ->
                    WideNavigationRailDefaults.windowInsets.only(WindowInsetsSides.Start)
                NavigationSuiteType.NavigationBar ->
                    NavigationBarDefaults.windowInsets.only(WindowInsetsSides.Bottom)
                NavigationSuiteType.NavigationRail ->
                    NavigationRailDefaults.windowInsets.only(WindowInsetsSides.Start)
                NavigationSuiteType.NavigationDrawer ->
                    DrawerDefaults.windowInsets.only(WindowInsetsSides.Start)
                else -> NoWindowInsets
            }
        }
    )

private val NoWindowInsets = WindowInsets(0, 0, 0, 0)

val LocalNavSharedTransitionScope = compositionLocalOf<SharedTransitionScope> {
    error("LocalNavSharedTransitionScope not provided")
}
