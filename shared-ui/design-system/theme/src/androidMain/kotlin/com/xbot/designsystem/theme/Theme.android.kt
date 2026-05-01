@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.designsystem.theme

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicTonalPalette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.dynamiccolor.ColorSpec

@Composable
internal actual fun rememberColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    amoled: Boolean,
    expressiveColor: Boolean
): ColorScheme {
    val context = LocalContext.current
    val contrastLevel = context.contrastLevel

    val seedColor = remember(dynamicColor) {
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val tonalPalette = dynamicTonalPalette(context)
            tonalPalette.primary40
        } else {
            AnilibriaSeedColor
        }
    }

    val style = if (expressiveColor) PaletteStyle.Expressive else PaletteStyle.TonalSpot

    return remember(seedColor, darkTheme, amoled, style, contrastLevel) {
        dynamicColorScheme(
            seedColor = seedColor,
            isDark = darkTheme,
            isAmoled = amoled,
            style = style,
            contrastLevel = contrastLevel.toDouble(),
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

private val Context.contrastLevel: Float
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        (getSystemService(Context.UI_MODE_SERVICE) as UiModeManager).contrast
    } else {
        0.0f
    }

@Composable
internal actual fun SystemAppearanceEffect(darkTheme: Boolean) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
    }
}