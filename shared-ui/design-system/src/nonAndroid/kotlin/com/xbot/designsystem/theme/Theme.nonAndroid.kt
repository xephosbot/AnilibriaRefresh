package com.xbot.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean
): ColorScheme {
    return remember(darkTheme) {
        if (darkTheme) darkScheme else lightScheme
    }
}
