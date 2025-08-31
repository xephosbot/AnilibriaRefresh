package com.xbot.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.xbot.resources.*
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource

@Composable
fun NotoSansFontFamily() = FontFamily(
    FontVariable(Res.font.notosans_variable, 100, 100f),
    FontVariable(Res.font.notosans_variable, 200, 100f),
    FontVariable(Res.font.notosans_variable, 300, 100f),
    FontVariable(Res.font.notosans_variable, 400, 100f),
    FontVariable(Res.font.notosans_variable, 500, 100f),
    FontVariable(Res.font.notosans_variable, 600, 100f),
    FontVariable(Res.font.notosans_variable, 700, 100f),
    FontVariable(Res.font.notosans_variable, 800, 100f),
    FontVariable(Res.font.notosans_variable, 900, 100f),
    FontVariable(Res.font.notosans_varialbe_italic, 100, 100f, FontStyle.Italic),
    FontVariable(Res.font.notosans_varialbe_italic, 200, 100f, FontStyle.Italic),
    FontVariable(Res.font.notosans_varialbe_italic, 300, 100f, FontStyle.Italic),
    FontVariable(Res.font.notosans_varialbe_italic, 400, 100f, FontStyle.Italic),
    FontVariable(Res.font.notosans_varialbe_italic, 500, 100f, FontStyle.Italic),
    FontVariable(Res.font.notosans_varialbe_italic, 600, 100f, FontStyle.Italic),
    FontVariable(Res.font.notosans_varialbe_italic, 700, 100f, FontStyle.Italic),
    FontVariable(Res.font.notosans_varialbe_italic, 800, 100f, FontStyle.Italic),
    FontVariable(Res.font.notosans_varialbe_italic, 900, 100f, FontStyle.Italic),
)

@Composable
fun BebesNeueFontFamily() = FontFamily(
    Font(Res.font.bebesneue_thin, FontWeight.Thin),
    Font(Res.font.bebesneue_light, FontWeight.Light),
    Font(Res.font.bebesneue_regular, FontWeight.Normal),
    Font(Res.font.bebesneue_bold, FontWeight.Bold),
)

// Default Material 3 typography values
val baseline = Typography()

@Composable
fun AnilibriaTypography() = Typography(
    displayLarge = baseline.displayLarge.applyDisplayFontFamily(),
    displayMedium = baseline.displayMedium.applyDisplayFontFamily(),
    displaySmall = baseline.displaySmall.applyDisplayFontFamily(),
    headlineLarge = baseline.headlineLarge.applyDisplayFontFamily(),
    headlineMedium = baseline.headlineMedium.applyDisplayFontFamily(),
    headlineSmall = baseline.headlineSmall.applyDisplayFontFamily(),
    titleLarge = baseline.titleLarge.applyDisplayFontFamily(),
    titleMedium = baseline.titleMedium.applyDisplayFontFamily(),
    titleSmall = baseline.titleSmall.applyDisplayFontFamily(),
    bodyLarge = baseline.bodyLarge.applyBodyFontFamily(),
    bodyMedium = baseline.bodyMedium.applyBodyFontFamily(),
    bodySmall = baseline.bodySmall.applyBodyFontFamily(),
    labelLarge = baseline.labelLarge.applyBodyFontFamily(),
    labelMedium = baseline.labelMedium.applyBodyFontFamily(),
    labelSmall = baseline.labelSmall.applyBodyFontFamily(),
)

@Composable
private fun TextStyle.applyDisplayFontFamily() = copy(
    fontFamily = BebesNeueFontFamily(),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both
    ),
    baselineShift = BaselineShift(-0.3f),
)

@Composable
private fun TextStyle.applyBodyFontFamily() = copy(
    fontFamily = NotoSansFontFamily(),
)

@Composable
private fun FontVariable(
    resource: FontResource,
    weight: Int,
    width: Float,
    style: FontStyle = FontStyle.Normal,
) = Font(
    resource,
    FontWeight(weight),
    style,
    FontVariation.Settings(
        FontVariation.weight(weight),
        FontVariation.width(width),
    )
)