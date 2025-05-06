package com.xbot.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
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

// Default Material 3 typography values
val baseline = Typography()

@Composable
fun AnilibriaTypography() = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = NotoSansFontFamily()),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = NotoSansFontFamily(),
        fontSize = 40.sp,
        lineHeight = 50.sp,
        fontWeight = FontWeight.Bold
    ),
    displaySmall = baseline.displaySmall.copy(fontFamily = NotoSansFontFamily()),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = NotoSansFontFamily()),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = NotoSansFontFamily()),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = NotoSansFontFamily()),
    titleLarge = baseline.titleLarge.copy(
        fontFamily = BebesNeueFontFamily(),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both
        ),
        baselineShift = BaselineShift(-0.3f),
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = BebesNeueFontFamily(),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both
        ),
        baselineShift = BaselineShift(-0.3f),
    ),
    titleSmall = baseline.titleSmall.copy(fontFamily = NotoSansFontFamily()),
    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = NotoSansFontFamily(),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = NotoSansFontFamily()),
    bodySmall = baseline.bodySmall.copy(fontFamily = NotoSansFontFamily()),
    labelLarge = baseline.labelLarge.copy(fontFamily = NotoSansFontFamily()),
    labelMedium = baseline.labelMedium.copy(fontFamily = NotoSansFontFamily()),
    labelSmall = baseline.labelSmall.copy(fontFamily = NotoSansFontFamily()),
)
