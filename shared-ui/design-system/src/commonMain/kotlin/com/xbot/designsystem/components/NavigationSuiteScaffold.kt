package com.xbot.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.ShortNavigationBarDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveComponentOverrideApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldWithPrimaryActionOverride
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldWithPrimaryActionOverrideScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.window.core.layout.WindowSizeClass
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun NavigationSuiteScaffold(
    navigationSuiteItems: NavigationSuiteScope.() -> Unit,
    modifier: Modifier = Modifier,
    state: NavigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState(),
    layoutType: NavigationSuiteType =
        com.xbot.designsystem.components.NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
    navigationSuiteColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(),
    containerColor: Color = NavigationSuiteScaffoldDefaults.containerColor,
    contentColor: Color = NavigationSuiteScaffoldDefaults.contentColor,
    content: @Composable () -> Unit
) {
    Surface(modifier = modifier, color = containerColor, contentColor = contentColor) {
        val scaffoldState = rememberScaffoldState()

        NavigationSuiteScaffoldLayout(
            navigationSuite = {
                NavigationSuite(
                    layoutType = layoutType,
                    colors = navigationSuiteColors,
                    content = navigationSuiteItems
                )
            },
            state = state,
            layoutType = layoutType,
            snackbar = {
                SnackbarHost(hostState = scaffoldState.snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        shape = MaterialTheme.shapes.medium,
                    )
                }
            },
            content = {
                Box(
                    Modifier.consumeWindowInsets(
                        if (
                            state.currentValue == NavigationSuiteScaffoldValue.Hidden &&
                            !state.isAnimating
                        ) {
                            NoWindowInsets
                        } else {
                            when (layoutType) {
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
                ) {
                    content()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun NavigationSuiteScaffoldLayout(
    navigationSuite: @Composable () -> Unit,
    layoutType: NavigationSuiteType =
        com.xbot.designsystem.components.NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
    state: NavigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState(),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    snackbar: @Composable () -> Unit = {},
    primaryActionContent: @Composable (() -> Unit) = {},
    primaryActionContentHorizontalAlignment: Alignment.Horizontal =
        NavigationSuiteScaffoldDefaults.primaryActionContentAlignment,
    content: @Composable () -> Unit = {}
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (state.currentValue == NavigationSuiteScaffoldValue.Hidden) 0f else 1f,
        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
    )

    Layout(
        contents = listOf(navigationSuite, snackbar, primaryActionContent, content)
    ) { (navigationSuiteMeasurable, snackbarMeasurable, primaryActionContentMeasurable, contentMeasurable), constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        // Find the navigation suite composable
        val navigationPlaceable = navigationSuiteMeasurable.first().measure(looseConstraints)
        val primaryActionContentPlaceable = primaryActionContentMeasurable.first().measure(looseConstraints)

        val isNavigationBar = layoutType.isNavigationBar

        val layoutHeight = constraints.maxHeight
        val layoutWidth = constraints.maxWidth

        val contentPlaceable = contentMeasurable
            .first()
            .measure(
                if (isNavigationBar) {
                    constraints.copy(
                        minHeight = (layoutHeight - (navigationPlaceable.height * animationProgress).toInt()).coerceAtLeast(0),
                        maxHeight = (layoutHeight - (navigationPlaceable.height * animationProgress).toInt()).coerceAtLeast(0)
                    )
                } else {
                    constraints.copy(
                        minWidth = (layoutWidth - (navigationPlaceable.width * animationProgress).toInt()).coerceAtLeast(0),
                        maxWidth = (layoutWidth - (navigationPlaceable.width * animationProgress).toInt()).coerceAtLeast(0)
                    )
                }
            )

        val leftInset = contentWindowInsets.getLeft(this, layoutDirection)
        val rightInset = contentWindowInsets.getRight(this, layoutDirection)
        val bottomInset = contentWindowInsets.getBottom(this)
        val snackbarPlaceable = snackbarMeasurable
            .first()
            .measure(
                looseConstraints.offset(
                    - rightInset - (if (!isNavigationBar) (navigationPlaceable.width * animationProgress).toInt() else leftInset),
                    -bottomInset,
                ),
            )

        val snackbarHeight = snackbarPlaceable.height
        val snackbarWidth = snackbarPlaceable.width

        layout(layoutWidth, layoutHeight) {
            if (isNavigationBar) {
                // Place content above the navigation component.
                contentPlaceable.placeRelative(0, 0)
                // Place the navigation component at the bottom of the screen.
                navigationPlaceable.placeRelative(
                    0,
                    layoutHeight - (navigationPlaceable.height * animationProgress).toInt()
                )

                // Place the primary action content above the navigation component.
                val positionX =
                    if (primaryActionContentHorizontalAlignment == Alignment.Start) {
                        PrimaryActionContentPadding.roundToPx()
                    } else if (
                        primaryActionContentHorizontalAlignment == Alignment.CenterHorizontally
                    ) {
                        (layoutWidth - primaryActionContentPlaceable.width) / 2
                    } else {
                        layoutWidth -
                                primaryActionContentPlaceable.width -
                                PrimaryActionContentPadding.roundToPx()
                    }
                primaryActionContentPlaceable.placeRelative(
                    positionX,
                    layoutHeight -
                            primaryActionContentPlaceable.height -
                            PrimaryActionContentPadding.roundToPx() -
                            (navigationPlaceable.height * animationProgress).toInt(),
                )

                snackbarPlaceable.placeRelative(
                    (layoutWidth - snackbarWidth) / 2 +
                            contentWindowInsets.getLeft(this@Layout, layoutDirection),
                    layoutHeight - (snackbarHeight + (navigationPlaceable.height * animationProgress).toInt()
                        .coerceAtLeast(contentWindowInsets.getBottom(this@Layout)))
                )
            } else {
                // Place the navigation component at the start of the screen.
                navigationPlaceable.placeRelative(
                    (0 - (navigationPlaceable.width * (1f - animationProgress))).toInt(),
                    0
                )

                // Place content to the side of the navigation component.
                contentPlaceable.placeRelative(
                    (navigationPlaceable.width * animationProgress).toInt(),
                    0
                )

                snackbarPlaceable.placeRelative(
                    (navigationPlaceable.width * animationProgress).toInt().let { navWidth ->
                        navWidth + (layoutWidth - navWidth - snackbarWidth) / 2
                    },
                    layoutHeight - (snackbarHeight + contentWindowInsets.getBottom(this@Layout))
                )
            }
        }
    }
}

/**
 * Remember and creates an instance of [ScaffoldState]
 */
@Composable
fun rememberScaffoldState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarManager: SnackbarManager = SnackbarManager,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ScaffoldState = remember(snackbarHostState, snackbarManager, coroutineScope) {
    ScaffoldState(snackbarHostState, snackbarManager, coroutineScope)
}

@Stable
class ScaffoldState(
    val snackbarHostState: SnackbarHostState,
    private val snackbarManager: SnackbarManager,
    coroutineScope: CoroutineScope,
) {
    init {
        coroutineScope.launch {
            println("StartScope")
            snackbarManager.messages.collect { currentMessages ->
                println(currentMessages.toString())
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = stringResource(message.title)
                    val actionLabel = message.action?.title?.let { stringResource(it) }
                    // Notify the SnackbarManager so it can remove the current message from the list
                    snackbarManager.setMessageShown(message.id)
                    // Display the snackbar on the screen. `showSnackbar` is a function
                    // that suspends until the snackbar disappears from the screen
                    val result = snackbarHostState.showSnackbar(
                        message = text,
                        actionLabel = actionLabel,
                    )

                    when (result) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> message.action?.action?.let { it() }
                    }
                }
            }
        }
    }
}

private val NoWindowInsets = WindowInsets(0, 0, 0, 0)

@OptIn(ExperimentalMaterial3AdaptiveComponentOverrideApi::class)
object AnilibriaNavigationSuiteScaffold : NavigationSuiteScaffoldWithPrimaryActionOverride {
    @Composable
    override fun NavigationSuiteScaffoldWithPrimaryActionOverrideScope.NavigationSuiteScaffoldWithPrimaryAction() {
        Surface(modifier = modifier, color = containerColor, contentColor = contentColor) {
            val scaffoldState = rememberScaffoldState()

            NavigationSuiteScaffoldLayout(
                navigationSuite = {
                    NavigationSuite(
                        navigationSuiteType = navigationSuiteType,
                        colors = navigationSuiteColors,
                        primaryActionContent = primaryActionContent,
                        verticalArrangement = navigationItemVerticalArrangement,
                        content = navigationItems,
                    )
                },
                layoutType = navigationSuiteType,
                state = state,
                primaryActionContent = primaryActionContent,
                primaryActionContentHorizontalAlignment = primaryActionContentHorizontalAlignment,
                snackbar = {
                    SnackbarHost(hostState = scaffoldState.snackbarHostState) { data ->
                        Snackbar(
                            snackbarData = data,
                            shape = MaterialTheme.shapes.medium,
                        )
                    }
                },
                content = {
                    Box(
                        Modifier.consumeWindowInsets(
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
                    ) {
                        content()
                    }
                },
            )
        }
    }
}

object NavigationSuiteScaffoldDefaults {
    fun calculateFromAdaptiveInfo(adaptiveInfo: WindowAdaptiveInfo): NavigationSuiteType {
        return with(adaptiveInfo) {
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
}

private val PrimaryActionContentPadding = 16.dp
private val NavigationSuiteType.isNavigationBar
    get() =
        this == NavigationSuiteType.ShortNavigationBarCompact ||
                this == NavigationSuiteType.ShortNavigationBarMedium ||
                this == NavigationSuiteType.NavigationBar
