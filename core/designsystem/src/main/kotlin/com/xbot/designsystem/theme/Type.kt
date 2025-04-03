package com.xbot.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.xbot.designsystem.R

val NotoSansFontFamily = FontFamily(
    Font(R.font.notosans_thin, FontWeight.Thin),
    Font(R.font.notosans_thinitalic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.notosans_extralight, FontWeight.ExtraLight),
    Font(R.font.notosans_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.notosans_light, FontWeight.Light),
    Font(R.font.notosans_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.notosans_regular, FontWeight.Normal),
    Font(R.font.notosans_regularitalic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.notosans_medium, FontWeight.Medium),
    Font(R.font.notosans_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.notosans_semibold, FontWeight.SemiBold),
    Font(R.font.notosans_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.notosans_bold, FontWeight.Bold),
    Font(R.font.notosans_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.notosans_extrabold, FontWeight.ExtraBold),
    Font(R.font.notosans_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.notosans_black, FontWeight.Black),
    Font(R.font.notosans_blackitalic, FontWeight.Black, FontStyle.Italic)
)

val BebesNeueFontFamily = FontFamily(
    Font(R.font.bebesneue_thin, FontWeight.Thin),
    Font(R.font.bebesneue_light, FontWeight.Light),
    Font(R.font.bebesneue_regular, FontWeight.Normal),
    Font(R.font.bebesneue_bold, FontWeight.Bold),
)

// Default Material 3 typography values
val baseline = Typography()

val AnilibriaTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = NotoSansFontFamily),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = NotoSansFontFamily,
        fontSize = 40.sp,
        lineHeight = 50.sp,
        fontWeight = FontWeight.SemiBold
    ),
    displaySmall = baseline.displaySmall.copy(fontFamily = NotoSansFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = NotoSansFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = NotoSansFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = NotoSansFontFamily),
    titleLarge = baseline.titleLarge.copy(
        fontFamily = BebesNeueFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = BebesNeueFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    titleSmall = baseline.titleSmall.copy(fontFamily = NotoSansFontFamily),
    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = NotoSansFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = NotoSansFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = NotoSansFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = NotoSansFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = NotoSansFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = NotoSansFontFamily),
)
