package com.xbot.designsystem.theme

import androidx.compose.foundation.OverscrollFactory
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.dynamiccolor.ColorSpec

@Composable
actual fun rememberColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    amoled: Boolean,
    expressiveColor: Boolean
): ColorScheme {
    val style = if (expressiveColor) PaletteStyle.Expressive else PaletteStyle.TonalSpot

    return remember(darkTheme, amoled, style, dynamicColor) {
        dynamicColorScheme(
            seedColor = Seed,
            isDark = darkTheme,
            isAmoled = amoled,
            style = style,
            specVersion = ColorSpec.SpecVersion.SPEC_2025,
        ) { colorScheme ->
            colorScheme.copy(
                surfaceContainer = if (amoled && darkTheme) {
                    Color.Black
                } else {
                    colorScheme.surfaceContainer
                }
            )
        }
    }
}

@Composable
actual fun rememberPlatformOverscrollFactory(): OverscrollFactory? {
    return null
}

@Composable
actual fun SystemAppearanceEffect(darkTheme: Boolean) {
    // No-op for non-Android platforms for now
}
