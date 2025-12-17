package com.xbot.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveComponentOverrideApi
import androidx.compose.material3.adaptive.navigationsuite.LocalNavigationSuiteScaffoldWithPrimaryActionOverride
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.xbot.designsystem.components.AnilibriaNavigationSuiteScaffold

@OptIn(
    ExperimentalMaterial3AdaptiveComponentOverrideApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
actual fun AnilibriaTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable (() -> Unit)
) {
    val colorScheme = rememberColorScheme(darkTheme, dynamicColor)

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
actual fun rememberColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean
): ColorScheme {
    return if (darkTheme) darkScheme else lightScheme
}
