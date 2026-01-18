package com.xbot.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ComponentOverrideApi
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveComponentOverrideApi
import androidx.compose.material3.adaptive.navigationsuite.LocalNavigationSuiteScaffoldWithPrimaryActionOverride
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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

    SystemAppearanceEffect(darkTheme)

    CompositionLocalProvider(
        LocalNavigationSuiteScaffoldWithPrimaryActionOverride provides AnilibriaNavigationSuiteScaffold
    ) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
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
expect fun SystemAppearanceEffect(darkTheme: Boolean)
