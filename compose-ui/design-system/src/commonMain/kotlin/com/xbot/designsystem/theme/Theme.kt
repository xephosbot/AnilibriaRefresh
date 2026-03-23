package com.xbot.designsystem.theme

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.OverscrollFactory
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ComponentOverrideApi
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveComponentOverrideApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.LocalNavigationSuiteScaffoldWithPrimaryActionOverride
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.xbot.designsystem.components.AnilibriaNavigationSuiteScaffold

@OptIn(
    ExperimentalMaterial3ComponentOverrideApi::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveComponentOverrideApi::class
)
@Composable
fun AnilibriaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    amoled: Boolean = false,
    expressiveColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = rememberColorScheme(darkTheme, dynamicColor, amoled, expressiveColor)
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val margins = if (windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
        Margins(horizontal = 24.dp)
    } else {
        Margins(horizontal = 16.dp)
    }

    SystemAppearanceEffect(darkTheme)

    CompositionLocalProvider(
        LocalOverscrollFactory provides rememberPlatformOverscrollFactory(),
        LocalNavigationSuiteScaffoldWithPrimaryActionOverride provides AnilibriaNavigationSuiteScaffold,
        LocalMargins provides margins
    ) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            shapes = Shapes,
            motionScheme = MotionScheme.expressive(),
            typography = AnilibriaTypography(),
            content = content,
        )
    }
}

@Composable
expect fun rememberColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    amoled: Boolean,
    expressiveColor: Boolean
): ColorScheme

@Composable
expect fun rememberPlatformOverscrollFactory(): OverscrollFactory?

@Composable
expect fun SystemAppearanceEffect(darkTheme: Boolean)
