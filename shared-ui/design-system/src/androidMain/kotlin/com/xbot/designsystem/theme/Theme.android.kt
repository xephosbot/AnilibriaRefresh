@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.designsystem.theme

import android.app.UiModeManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicTonalPalette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val contrastLevel = context.contrastLevel

    val seedColor = remember(dynamicColor) {
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val tonalPalette = dynamicTonalPalette(context)
            tonalPalette.primary40
        } else {
            Seed
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

@Composable
actual fun SystemAppearanceEffect(darkTheme: Boolean) {
    val context = LocalContext.current
    DisposableEffect(darkTheme) {
        val activity = context.findActivity()
        activity?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ) { darkTheme },
            navigationBarStyle = SystemBarStyle.auto(
                DefaultLightScrim,
                DefaultDarkScrim,
            ) { darkTheme }
        )
        onDispose {}
    }
}

private fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private val Context.contrastLevel: Float
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        (getSystemService(Context.UI_MODE_SERVICE) as UiModeManager).contrast
    } else {
        0.0f
    }

private val DefaultLightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DefaultDarkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
