package com.xbot.anilibriarefresh.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.xbot.anilibriarefresh.navigation.NavigationContentPosition
import com.xbot.anilibriarefresh.navigation.NavigationSuiteType

@Composable
fun AnilibriaNavigationSuiteScaffold(
    modifier: Modifier = Modifier,
    navigationSuite: @Composable NavigationSuiteScope.() -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
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
        NavigationSuiteScaffoldLayout(
            layoutType = navLayoutType,
            navigationSuite = {
                NavigationSuiteScopeImpl(navLayoutType, navContentPosition).navigationSuite()
            },
            content = content
        )
    }
}

@Composable
private fun NavigationSuiteScaffoldLayout(
    navigationSuite: @Composable () -> Unit,
    layoutType: NavigationSuiteType,
    content: @Composable () -> Unit
) {
    var navigationPadding by remember { mutableStateOf(PaddingValues()) }
    val contentBox = @Composable {
        Box(
            modifier = Modifier.consumeWindowInsets(
                when(layoutType) {
                    NavigationSuiteType.NavigationRail -> {
                        WindowInsets.displayCutout.only(WindowInsetsSides.Start)
                    }
                    NavigationSuiteType.NavigationBar -> {
                        WindowInsets(0.dp)
                    }
                }
            )
        ) {
            CompositionLocalProvider(
                LocalNavigationPadding provides navigationPadding
            ) {
                content()
            }
        }
    }

    Layout(
        contents = listOf(navigationSuite, contentBox)
    ) { (navigationSuiteMeasurable, contentMeasurable), constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        // Find the navigation suite composable
        val navigationPlaceable = navigationSuiteMeasurable.first().measure(looseConstraints)

        val isNavigationBar = layoutType == NavigationSuiteType.NavigationBar

        if (isNavigationBar) {
            navigationPadding = PaddingValues(bottom = navigationPlaceable.height.toDp())
        }

        val layoutHeight = constraints.maxHeight
        val layoutWidth = constraints.maxWidth

        val contentPlaceable = contentMeasurable.first().measure(
            if (!isNavigationBar) {
                constraints.copy(
                    minWidth = layoutWidth - navigationPlaceable.width,
                    maxWidth = layoutWidth - navigationPlaceable.width
                )
            } else {
                constraints
            }
        )

        layout(layoutWidth, layoutHeight) {
            if (isNavigationBar) {
                // Place content above the navigation component.
                contentPlaceable.place(0, 0)
                // Place the navigation component at the bottom of the screen.
                navigationPlaceable.place(0, layoutHeight - (navigationPlaceable.height))
            } else {
                // Place content to the side of the navigation component.
                contentPlaceable.place(navigationPlaceable.width, 0)

                // Place the navigation component at the start of the screen.
                navigationPlaceable.place(0, 0)
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

val LocalNavigationPadding = compositionLocalOf { PaddingValues() }

private fun WindowSizeClass.isCompact() = windowWidthSizeClass == WindowWidthSizeClass.COMPACT
