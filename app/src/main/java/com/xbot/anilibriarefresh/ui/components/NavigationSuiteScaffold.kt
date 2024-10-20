@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.onConsumedWindowInsetsChanged
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.internal.MutableWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.xbot.anilibriarefresh.navigation.NavigationContentPosition
import com.xbot.anilibriarefresh.navigation.NavigationSuiteType

@Composable
fun AnilibriaNavigationSuiteScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    navigationSuite: @Composable NavigationSuiteScope.() -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    content: @Composable (PaddingValues) -> Unit
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
        WindowHeightSizeClass.EXPANDED -> NavigationContentPosition.CENTER

        else -> NavigationContentPosition.TOP
    }

    val safeInsets = remember(contentWindowInsets) { MutableWindowInsets(contentWindowInsets) }
    Surface(
        modifier = modifier.onConsumedWindowInsetsChanged { consumedWindowInsets ->
            // Exclude currently consumed window insets from user provided contentWindowInsets
            safeInsets.insets = contentWindowInsets.exclude(consumedWindowInsets)
        },
        color = containerColor,
        contentColor = contentColor
    ) {
        NavigationSuiteScaffoldLayout(
            layoutType = navLayoutType,
            topBar = topBar,
            snackbar = {
                SnackbarHost(hostState = scaffoldState.snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        shape = MaterialTheme.shapes.medium
                    )
                }
            },
            navigationSuite = {
                NavigationSuiteScopeImpl(navLayoutType, navContentPosition).navigationSuite()
            },
            content = content,
            contentWindowInsets = contentWindowInsets
        )
    }
}

@Composable
private fun NavigationSuiteScaffoldLayout(
    navigationSuite: @Composable () -> Unit,
    topBar: @Composable () -> Unit = {},
    snackbar: @Composable () -> Unit,
    layoutType: NavigationSuiteType,
    content: @Composable (PaddingValues) -> Unit = {},
    contentWindowInsets: WindowInsets,
) {
    SubcomposeLayout { constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val topBarPlaceables = subcompose(NavigationSuiteScaffoldLayoutContent.TopBar, topBar).fastMap {
            it.measure(looseConstraints)
        }

        val topBarHeight = topBarPlaceables.fastMaxBy { it.height }?.height ?: 0

        val snackbarPlaceables = subcompose(NavigationSuiteScaffoldLayoutContent.Snackbar, snackbar).fastMap {
            // respect only bottom and horizontal for snackbar and fab
            val leftInset = contentWindowInsets
                .getLeft(this@SubcomposeLayout, layoutDirection)
            val rightInset = contentWindowInsets
                .getRight(this@SubcomposeLayout, layoutDirection)
            val bottomInset = contentWindowInsets.getBottom(this@SubcomposeLayout)
            // offset the snackbar constraints by the insets values
            it.measure(
                looseConstraints.offset(
                    -leftInset - rightInset,
                    -bottomInset
                )
            )
        }

        val snackbarHeight = snackbarPlaceables.fastMaxBy { it.height }?.height ?: 0
        val snackbarWidth = snackbarPlaceables.fastMaxBy { it.width }?.width ?: 0

        // Find the navigation suite composable
        val navigationPlaceables =
            subcompose(NavigationSuiteScaffoldLayoutContent.Navigation) { navigationSuite() }
                .fastMap { it.measure(looseConstraints) }

        val navigationBarHeight = navigationPlaceables.fastMaxBy { it.height }?.height
        val navigationBarWidth = navigationPlaceables.fastMaxBy { it.width }?.width

        val isNavigationBar = layoutType == NavigationSuiteType.NavigationBar
        val layoutHeight = constraints.maxHeight
        val layoutWidth = constraints.maxWidth

        val snackbarOffsetFromBottom = if (snackbarHeight != 0) {
            snackbarHeight + (if (isNavigationBar && navigationBarHeight != null)  navigationBarHeight else
                contentWindowInsets.getBottom(this@SubcomposeLayout))
        } else {
            0
        }

        val contentPlaceables = subcompose(NavigationSuiteScaffoldLayoutContent.MainContent) {
            val insets = contentWindowInsets.asPaddingValues(this@SubcomposeLayout)
            val innerPadding = PaddingValues(
                top = if (!isNavigationBar || topBarPlaceables.isEmpty()) {
                    insets.calculateTopPadding()
                } else {
                    topBarHeight.toDp()
                },
                bottom = if (!isNavigationBar || navigationPlaceables.isEmpty() || navigationBarHeight == null) {
                    insets.calculateBottomPadding()
                } else {
                    navigationBarHeight.toDp()
                },
                start = if (isNavigationBar || navigationPlaceables.isEmpty() || navigationBarWidth == null) {
                    insets.calculateStartPadding((this@SubcomposeLayout).layoutDirection)
                } else {
                    navigationBarWidth.toDp()
                },
                end = insets.calculateEndPadding((this@SubcomposeLayout).layoutDirection)
            )
            content(innerPadding)
        }.fastMap {
            it.measure(looseConstraints)
        }

        layout(layoutWidth, layoutHeight) {
            if (isNavigationBar) {
                // Place content above the navigation component.
                contentPlaceables.fastForEach {
                    it.place(0, 0)
                }
                topBarPlaceables.fastForEach {
                    it.place(0, 0)
                }
                snackbarPlaceables.fastForEach {
                    it.place(
                        (layoutWidth - snackbarWidth) / 2 +
                                contentWindowInsets.getLeft(this@SubcomposeLayout, layoutDirection),
                        layoutHeight - snackbarOffsetFromBottom
                    )
                }
                // Place the navigation component at the bottom of the screen.
                navigationPlaceables.fastForEach {
                    it.place(0, layoutHeight - (navigationBarHeight ?: 0))
                }
            } else {
                // Place content to the side of the navigation component.
                contentPlaceables.fastForEach {
                    it.place(0, 0)
                }
                snackbarPlaceables.fastForEach {
                    it.place(
                        (layoutWidth - snackbarWidth) / 2 +
                                contentWindowInsets.getLeft(this@SubcomposeLayout, layoutDirection),
                        layoutHeight - snackbarOffsetFromBottom
                    )
                }
                // Place the navigation component at the start of the screen.
                navigationPlaceables.fastForEach {
                    it.place(0, 0)
                }
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
    override val contentPosition: NavigationContentPosition
) : NavigationSuiteScope

private enum class NavigationSuiteScaffoldLayoutContent { MainContent, Navigation, TopBar, Snackbar }

private fun WindowSizeClass.isCompact() = windowWidthSizeClass == WindowWidthSizeClass.COMPACT