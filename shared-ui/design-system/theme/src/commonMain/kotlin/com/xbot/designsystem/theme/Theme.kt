package com.xbot.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class,)
@Composable
fun AnilibriaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    amoled: Boolean = false,
    expressiveColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = rememberColorScheme(darkTheme, dynamicColor, amoled, expressiveColor)
    val margins = remember { Margins(horizontal = 16.dp) }

    if (!LocalInspectionMode.current) {
        SystemAppearanceEffect(darkTheme)
    }

    CompositionLocalProvider(
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
internal expect fun rememberColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    amoled: Boolean,
    expressiveColor: Boolean
): ColorScheme

@Composable
internal expect fun SystemAppearanceEffect(darkTheme: Boolean)
