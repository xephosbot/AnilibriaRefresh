@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.designsystem.theme

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicTonalPalette
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.dynamiccolor.ColorSpec

internal val mediumContrastLightColorScheme = lightColorScheme(
    primary = PrimaryLightMediumContrast,
    onPrimary = OnPrimaryLightMediumContrast,
    primaryContainer = PrimaryContainerLightMediumContrast,
    onPrimaryContainer = OnPrimaryContainerLightMediumContrast,
    inversePrimary = InversePrimaryLightMediumContrast,
    secondary = SecondaryLightMediumContrast,
    onSecondary = OnSecondaryLightMediumContrast,
    secondaryContainer = SecondaryContainerLightMediumContrast,
    onSecondaryContainer = OnSecondaryContainerLightMediumContrast,
    tertiary = TertiaryLightMediumContrast,
    onTertiary = OnTertiaryLightMediumContrast,
    tertiaryContainer = TertiaryContainerLightMediumContrast,
    onTertiaryContainer = OnTertiaryContainerLightMediumContrast,
    background = BackgroundLightMediumContrast,
    onBackground = OnBackgroundLightMediumContrast,
    surface = SurfaceLightMediumContrast,
    onSurface = OnSurfaceLightMediumContrast,
    surfaceVariant = SurfaceVariantLightMediumContrast,
    onSurfaceVariant = OnSurfaceVariantLightMediumContrast,
    surfaceTint = SurfaceTintLightMediumContrast,
    inverseSurface = InverseSurfaceLightMediumContrast,
    inverseOnSurface = InverseOnSurfaceLightMediumContrast,
    error = ErrorLightMediumContrast,
    onError = OnErrorLightMediumContrast,
    errorContainer = ErrorContainerLightMediumContrast,
    onErrorContainer = OnErrorContainerLightMediumContrast,
    outline = OutlineLightMediumContrast,
    outlineVariant = OutlineVariantLightMediumContrast,
    scrim = ScrimLightMediumContrast,
    surfaceBright = SurfaceBrightLightMediumContrast,
    surfaceContainer = SurfaceContainerLightMediumContrast,
    surfaceContainerHigh = SurfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = SurfaceContainerHighestLightMediumContrast,
    surfaceContainerLow = SurfaceContainerLowLightMediumContrast,
    surfaceContainerLowest = SurfaceContainerLowestLightMediumContrast,
    surfaceDim = SurfaceDimLightMediumContrast,
    primaryFixed = PrimaryFixedMediumContrast,
    primaryFixedDim = PrimaryFixedDimMediumContrast,
    onPrimaryFixed = OnPrimaryFixedMediumContrast,
    onPrimaryFixedVariant = OnPrimaryFixedVariantMediumContrast,
    secondaryFixed = SecondaryFixedMediumContrast,
    secondaryFixedDim = SecondaryFixedDimMediumContrast,
    onSecondaryFixed = OnSecondaryFixedMediumContrast,
    onSecondaryFixedVariant = OnSecondaryFixedVariantMediumContrast,
    tertiaryFixed = TertiaryFixedMediumContrast,
    tertiaryFixedDim = TertiaryFixedDimMediumContrast,
    onTertiaryFixed = OnTertiaryFixedMediumContrast,
    onTertiaryFixedVariant = OnTertiaryFixedVariantMediumContrast,
)

internal val mediumContrastDarkColorScheme = darkColorScheme(
    primary = PrimaryDarkMediumContrast,
    onPrimary = OnPrimaryDarkMediumContrast,
    primaryContainer = PrimaryContainerDarkMediumContrast,
    onPrimaryContainer = OnPrimaryContainerDarkMediumContrast,
    inversePrimary = InversePrimaryDarkMediumContrast,
    secondary = SecondaryDarkMediumContrast,
    onSecondary = OnSecondaryDarkMediumContrast,
    secondaryContainer = SecondaryContainerDarkMediumContrast,
    onSecondaryContainer = OnSecondaryContainerDarkMediumContrast,
    tertiary = TertiaryDarkMediumContrast,
    onTertiary = OnTertiaryDarkMediumContrast,
    tertiaryContainer = TertiaryContainerDarkMediumContrast,
    onTertiaryContainer = OnTertiaryContainerDarkMediumContrast,
    background = BackgroundDarkMediumContrast,
    onBackground = OnBackgroundDarkMediumContrast,
    surface = SurfaceDarkMediumContrast,
    onSurface = OnSurfaceDarkMediumContrast,
    surfaceVariant = SurfaceVariantDarkMediumContrast,
    onSurfaceVariant = OnSurfaceVariantDarkMediumContrast,
    surfaceTint = SurfaceTintDarkMediumContrast,
    inverseSurface = InverseSurfaceDarkMediumContrast,
    inverseOnSurface = InverseOnSurfaceDarkMediumContrast,
    error = ErrorDarkMediumContrast,
    onError = OnErrorDarkMediumContrast,
    errorContainer = ErrorContainerDarkMediumContrast,
    onErrorContainer = OnErrorContainerDarkMediumContrast,
    outline = OutlineDarkMediumContrast,
    outlineVariant = OutlineVariantDarkMediumContrast,
    scrim = ScrimDarkMediumContrast,
    surfaceBright = SurfaceBrightDarkMediumContrast,
    surfaceContainer = SurfaceContainerDarkMediumContrast,
    surfaceContainerHigh = SurfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = SurfaceContainerHighestDarkMediumContrast,
    surfaceContainerLow = SurfaceContainerLowDarkMediumContrast,
    surfaceContainerLowest = SurfaceContainerLowestDarkMediumContrast,
    surfaceDim = SurfaceDimDarkMediumContrast,
    primaryFixed = PrimaryFixedMediumContrast,
    primaryFixedDim = PrimaryFixedDimMediumContrast,
    onPrimaryFixed = OnPrimaryFixedMediumContrast,
    onPrimaryFixedVariant = OnPrimaryFixedVariantMediumContrast,
    secondaryFixed = SecondaryFixedMediumContrast,
    secondaryFixedDim = SecondaryFixedDimMediumContrast,
    onSecondaryFixed = OnSecondaryFixedMediumContrast,
    onSecondaryFixedVariant = OnSecondaryFixedVariantMediumContrast,
    tertiaryFixed = TertiaryFixedMediumContrast,
    tertiaryFixedDim = TertiaryFixedDimMediumContrast,
    onTertiaryFixed = OnTertiaryFixedMediumContrast,
    onTertiaryFixedVariant = OnTertiaryFixedVariantMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = PrimaryLightHighContrast,
    onPrimary = OnPrimaryLightHighContrast,
    primaryContainer = PrimaryContainerLightHighContrast,
    onPrimaryContainer = OnPrimaryContainerLightHighContrast,
    inversePrimary = InversePrimaryLightHighContrast,
    secondary = SecondaryLightHighContrast,
    onSecondary = OnSecondaryLightHighContrast,
    secondaryContainer = SecondaryContainerLightHighContrast,
    onSecondaryContainer = OnSecondaryContainerLightHighContrast,
    tertiary = TertiaryLightHighContrast,
    onTertiary = OnTertiaryLightHighContrast,
    tertiaryContainer = TertiaryContainerLightHighContrast,
    onTertiaryContainer = OnTertiaryContainerLightHighContrast,
    background = BackgroundLightHighContrast,
    onBackground = OnBackgroundLightHighContrast,
    surface = SurfaceLightHighContrast,
    onSurface = OnSurfaceLightHighContrast,
    surfaceVariant = SurfaceVariantLightHighContrast,
    onSurfaceVariant = OnSurfaceVariantLightHighContrast,
    surfaceTint = SurfaceTintLightHighContrast,
    inverseSurface = InverseSurfaceLightHighContrast,
    inverseOnSurface = InverseOnSurfaceLightHighContrast,
    error = ErrorLightHighContrast,
    onError = OnErrorLightHighContrast,
    errorContainer = ErrorContainerLightHighContrast,
    onErrorContainer = OnErrorContainerLightHighContrast,
    outline = OutlineLightHighContrast,
    outlineVariant = OutlineVariantLightHighContrast,
    scrim = ScrimLightHighContrast,
    surfaceBright = SurfaceBrightLightHighContrast,
    surfaceContainer = SurfaceContainerLightHighContrast,
    surfaceContainerHigh = SurfaceContainerHighLightHighContrast,
    surfaceContainerHighest = SurfaceContainerHighestLightHighContrast,
    surfaceContainerLow = SurfaceContainerLowLightHighContrast,
    surfaceContainerLowest = SurfaceContainerLowestLightHighContrast,
    surfaceDim = SurfaceDimLightHighContrast,
    primaryFixed = PrimaryFixedHighContrast,
    primaryFixedDim = PrimaryFixedDimHighContrast,
    onPrimaryFixed = OnPrimaryFixedHighContrast,
    onPrimaryFixedVariant = OnPrimaryFixedVariantHighContrast,
    secondaryFixed = SecondaryFixedHighContrast,
    secondaryFixedDim = SecondaryFixedDimHighContrast,
    onSecondaryFixed = OnSecondaryFixedHighContrast,
    onSecondaryFixedVariant = OnSecondaryFixedVariantHighContrast,
    tertiaryFixed = TertiaryFixedHighContrast,
    tertiaryFixedDim = TertiaryFixedDimHighContrast,
    onTertiaryFixed = OnTertiaryFixedHighContrast,
    onTertiaryFixedVariant = OnTertiaryFixedVariantHighContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = PrimaryDarkHighContrast,
    onPrimary = OnPrimaryDarkHighContrast,
    primaryContainer = PrimaryContainerDarkHighContrast,
    onPrimaryContainer = OnPrimaryContainerDarkHighContrast,
    inversePrimary = InversePrimaryDarkHighContrast,
    secondary = SecondaryDarkHighContrast,
    onSecondary = OnSecondaryDarkHighContrast,
    secondaryContainer = SecondaryContainerDarkHighContrast,
    onSecondaryContainer = OnSecondaryContainerDarkHighContrast,
    tertiary = TertiaryDarkHighContrast,
    onTertiary = OnTertiaryDarkHighContrast,
    tertiaryContainer = TertiaryContainerDarkHighContrast,
    onTertiaryContainer = OnTertiaryContainerDarkHighContrast,
    background = BackgroundDarkHighContrast,
    onBackground = OnBackgroundDarkHighContrast,
    surface = SurfaceDarkHighContrast,
    onSurface = OnSurfaceDarkHighContrast,
    surfaceVariant = SurfaceVariantDarkHighContrast,
    onSurfaceVariant = OnSurfaceVariantDarkHighContrast,
    surfaceTint = SurfaceTintDarkHighContrast,
    inverseSurface = InverseSurfaceDarkHighContrast,
    inverseOnSurface = InverseOnSurfaceDarkHighContrast,
    error = ErrorDarkHighContrast,
    onError = OnErrorDarkHighContrast,
    errorContainer = ErrorContainerDarkHighContrast,
    onErrorContainer = OnErrorContainerDarkHighContrast,
    outline = OutlineDarkHighContrast,
    outlineVariant = OutlineVariantDarkHighContrast,
    scrim = ScrimDarkHighContrast,
    surfaceBright = SurfaceBrightDarkHighContrast,
    surfaceContainer = SurfaceContainerDarkHighContrast,
    surfaceContainerHigh = SurfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = SurfaceContainerHighestDarkHighContrast,
    surfaceContainerLow = SurfaceContainerLowDarkHighContrast,
    surfaceContainerLowest = SurfaceContainerLowestDarkHighContrast,
    surfaceDim = SurfaceDimDarkHighContrast,
    primaryFixed = PrimaryFixedHighContrast,
    primaryFixedDim = PrimaryFixedDimHighContrast,
    onPrimaryFixed = OnPrimaryFixedHighContrast,
    onPrimaryFixedVariant = OnPrimaryFixedVariantHighContrast,
    secondaryFixed = SecondaryFixedHighContrast,
    secondaryFixedDim = SecondaryFixedDimHighContrast,
    onSecondaryFixed = OnSecondaryFixedHighContrast,
    onSecondaryFixedVariant = OnSecondaryFixedVariantHighContrast,
    tertiaryFixed = TertiaryFixedHighContrast,
    tertiaryFixedDim = TertiaryFixedDimHighContrast,
    onTertiaryFixed = OnTertiaryFixedHighContrast,
    onTertiaryFixedVariant = OnTertiaryFixedVariantHighContrast,
)

@Composable
actual fun rememberColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean
): ColorScheme {
    val context = LocalContext.current
    return remember(darkTheme, dynamicColor) {
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val tonalPalette = dynamicTonalPalette(context)
                dynamicColorScheme(
                    seedColor = tonalPalette.primary80,
                    isDark = darkTheme,
                    style = PaletteStyle.Expressive,
                    specVersion = ColorSpec.SpecVersion.SPEC_2025,
                )
            }
            else -> selectSchemeForContrast(context, darkTheme)
        }
    }
}

internal fun selectSchemeForContrast(context: Context, darkTheme: Boolean): ColorScheme {
    var colorScheme = if (darkTheme) darkScheme else lightScheme
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val contrastLevel = uiModeManager.contrast

        colorScheme = when (contrastLevel) {
            in 0.0f..0.33f -> if (darkTheme) darkScheme else lightScheme

            in 0.34f..0.66f -> if (darkTheme)
                mediumContrastDarkColorScheme else mediumContrastLightColorScheme

            in 0.67f..1.0f -> if (darkTheme)
                highContrastDarkColorScheme else highContrastLightColorScheme

            else -> if (darkTheme) darkScheme else lightScheme
        }
        return colorScheme
    } else return colorScheme
}
