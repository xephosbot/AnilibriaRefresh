package com.xbot.anilibriarefresh.ui.components

import android.content.res.Resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.MutableWindowInsets
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.onConsumedWindowInsetsChanged
import androidx.compose.foundation.layout.only
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.offset
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.xbot.anilibriarefresh.navigation.NavigationContentPosition
import com.xbot.anilibriarefresh.navigation.NavigationSuiteType
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.anilibriarefresh.ui.utils.resources
import com.xbot.anilibriarefresh.ui.utils.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnilibriaNavigationSuiteScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navigationSuite: @Composable NavigationSuiteScope.() -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable () -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()

    val navLayoutType = when {
        adaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
        adaptiveInfo.windowSizeClass.isCompact() -> NavigationSuiteType.NavigationBar
        else -> NavigationSuiteType.NavigationRail
    }

    val navContentPosition = when (adaptiveInfo.windowSizeClass.windowHeightSizeClass) {
        WindowHeightSizeClass.COMPACT -> NavigationContentPosition.TOP
        WindowHeightSizeClass.MEDIUM,
        WindowHeightSizeClass.EXPANDED,
            -> NavigationContentPosition.CENTER

        else -> NavigationContentPosition.TOP
    }

    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
    ) {
        val safeInsets = remember { MutableWindowInsets(contentWindowInsets) }

        NavigationSuiteScaffoldLayout(
            layoutType = navLayoutType,
            navigationSuite = {
                NavigationSuiteScopeImpl(navLayoutType, navContentPosition).navigationSuite()
            },
            snackbar = {
                SnackbarHost(hostState = scaffoldState.snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        shape = MaterialTheme.shapes.medium,
                    )
                }
            },
            contentWindowInsets = safeInsets,
            content = {
                Box(
                    modifier = Modifier
                        .consumeWindowInsets(
                            when (navLayoutType) {
                                NavigationSuiteType.NavigationBar ->
                                    NavigationBarDefaults.windowInsets.only(WindowInsetsSides.Bottom)

                                NavigationSuiteType.NavigationRail ->
                                    NavigationRailDefaults.windowInsets.only(WindowInsetsSides.Start)
                            }
                        )
                        .onConsumedWindowInsetsChanged { consumedWindowInsets ->
                            safeInsets.insets = contentWindowInsets.exclude(consumedWindowInsets)
                        }
                ) {
                    content()
                }
            }
        )
    }
}

@Composable
private fun NavigationSuiteScaffoldLayout(
    navigationSuite: @Composable () -> Unit,
    layoutType: NavigationSuiteType,
    snackbar: @Composable () -> Unit,
    contentWindowInsets: WindowInsets,
    content: @Composable () -> Unit
) {
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
                        minHeight = (layoutHeight - navigationPlaceable.height).coerceAtLeast(0),
                        maxHeight = (layoutHeight - navigationPlaceable.height).coerceAtLeast(0)
                    )
                } else {
                    constraints.copy(
                        minWidth = (layoutWidth - navigationPlaceable.width).coerceAtLeast(0),
                        maxWidth = (layoutWidth - navigationPlaceable.width).coerceAtLeast(0)
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
                contentPlaceable.place(0, 0)
                // Place the navigation component at the bottom of the screen.
                navigationPlaceable.place(0, layoutHeight - (navigationPlaceable.height))

                snackbarPlaceable.place(
                    (layoutWidth - snackbarWidth) / 2 +
                            contentWindowInsets.getLeft(this@Layout, layoutDirection),
                    layoutHeight - (snackbarHeight + navigationPlaceable.height)
                )
            } else {
                // Place content to the side of the navigation component.
                contentPlaceable.place(navigationPlaceable.width, 0)

                // Place the navigation component at the start of the screen.
                navigationPlaceable.place(0, 0)

                snackbarPlaceable.place(
                    (layoutWidth - snackbarWidth - navigationPlaceable.width) / 2 +
                            contentWindowInsets.getLeft(this@Layout, layoutDirection),
                    layoutHeight - contentWindowInsets.getBottom(this@Layout)
                )
            }
        }
    }
}

interface NavigationSuiteScope {
    val layoutType: NavigationSuiteType
    val contentPosition: NavigationContentPosition
}

private class NavigationSuiteScopeImpl(
    override val layoutType: NavigationSuiteType,
    override val contentPosition: NavigationContentPosition,
) : NavigationSuiteScope

private fun WindowSizeClass.isCompact() = windowWidthSizeClass == WindowWidthSizeClass.COMPACT

/**
 * Remember and creates an instance of [ScaffoldState]
 */
@Composable
fun rememberScaffoldState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ScaffoldState = remember(snackbarHostState, snackbarManager, resources, coroutineScope) {
    ScaffoldState(snackbarHostState, snackbarManager, resources, coroutineScope)
}

@Stable
class ScaffoldState(
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
