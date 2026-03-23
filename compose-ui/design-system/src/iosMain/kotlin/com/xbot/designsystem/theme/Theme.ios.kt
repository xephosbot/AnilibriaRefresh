package com.xbot.designsystem.theme

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.OverscrollFactory
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.dynamiccolor.ColorSpec
import com.xbot.designsystem.utils.CupertinoOverscrollEffect

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
    val density = LocalDensity.current
    return CupertinoOverscrollEffectFactory(density)
}

private data class CupertinoOverscrollEffectFactory(
    private val density: Density
) : OverscrollFactory {
    override fun createOverscrollEffect(): OverscrollEffect {
        return CupertinoOverscrollEffect(density.density, applyClip = false)
    }
}

@Composable
actual fun SystemAppearanceEffect(darkTheme: Boolean) {
    // No-op for non-Android platforms for now
}
