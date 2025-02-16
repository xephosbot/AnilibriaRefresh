package com.xbot.designsystem.components

import android.content.res.Resources
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveComponentOverrideApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldComponentOverride
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldComponentOverrideContext
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.offset
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.resources
import com.xbot.designsystem.utils.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun NavigationSuiteScaffoldLayout(
    navigationSuite: @Composable () -> Unit,
    layoutType: NavigationSuiteType =
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
    state: NavigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState(),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    snackbar: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (state.currentValue == NavigationSuiteScaffoldValue.Hidden) 0f else 1f,
        animationSpec = AnimationSpec
    )

    Layout(
        contents = listOf(navigationSuite, snackbar, content)
    ) { (navigationSuiteMeasurable, snackbarMeasurable, contentMeasurable), constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        // Find the navigation suite composable
        val navigationPlaceable = navigationSuiteMeasurable.first().measure(looseConstraints)

        val isNavigationBar = layoutType == NavigationSuiteType.NavigationBar

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
        val rightInset =
            contentWindowInsets.getRight(this, layoutDirection)
        val bottomInset = contentWindowInsets.getBottom(this)
        val snackbarPlaceable = snackbarMeasurable
            .first()
            .measure(
                looseConstraints.offset(
                    -leftInset - rightInset,
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
                    (layoutWidth - snackbarWidth - navigationPlaceable.width) / 2 +
                            contentWindowInsets.getLeft(this@Layout, layoutDirection),
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
internal fun rememberScaffoldState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ScaffoldState = remember(snackbarHostState, snackbarManager, resources, coroutineScope) {
    ScaffoldState(snackbarHostState, snackbarManager, resources, coroutineScope)
}

@Stable
internal class ScaffoldState(
    val snackbarHostState: SnackbarHostState,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope,
) {
    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = resources.stringResource(message.title)
                    val actionLabel = message.action?.title?.let { resources.stringResource(it) }
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

private const val SpringDefaultSpatialDamping = 0.9f
private const val SpringDefaultSpatialStiffness = 700.0f

private val NoWindowInsets = WindowInsets(0, 0, 0, 0)
private val AnimationSpec: SpringSpec<Float> =
    spring(dampingRatio = SpringDefaultSpatialDamping, stiffness = SpringDefaultSpatialStiffness)

@OptIn(ExperimentalMaterial3AdaptiveComponentOverrideApi::class)
object AnilibriaNavigationSuiteScaffold : NavigationSuiteScaffoldComponentOverride {
    @Composable
    override fun NavigationSuiteScaffoldComponentOverrideContext.NavigationSuiteScaffold() {
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
}

object NavigationSuiteScaffoldDefaults {
    fun calculateFromAdaptiveInfo(adaptiveInfo: WindowAdaptiveInfo): NavigationSuiteType {
        return when {
            adaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
            adaptiveInfo.windowSizeClass.isCompact() -> NavigationSuiteType.NavigationBar
            else -> NavigationSuiteType.NavigationRail
        }
    }
}

private fun WindowSizeClass.isCompact() = windowWidthSizeClass == WindowWidthSizeClass.COMPACT