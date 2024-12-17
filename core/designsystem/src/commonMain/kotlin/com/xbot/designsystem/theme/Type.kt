package com.xbot.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.xbot.designsystem.resources.Res
import com.xbot.designsystem.resources.notosans_black
import com.xbot.designsystem.resources.notosans_blackitalic
import com.xbot.designsystem.resources.notosans_bold
import com.xbot.designsystem.resources.notosans_bolditalic
import com.xbot.designsystem.resources.notosans_extrabold
import com.xbot.designsystem.resources.notosans_extrabolditalic
import com.xbot.designsystem.resources.notosans_extralight
import com.xbot.designsystem.resources.notosans_extralightitalic
import com.xbot.designsystem.resources.notosans_light
import com.xbot.designsystem.resources.notosans_lightitalic
import com.xbot.designsystem.resources.notosans_medium
import com.xbot.designsystem.resources.notosans_mediumitalic
import com.xbot.designsystem.resources.notosans_regular
import com.xbot.designsystem.resources.notosans_regularitalic
import com.xbot.designsystem.resources.notosans_semibold
import com.xbot.designsystem.resources.notosans_semibolditalic
import com.xbot.designsystem.resources.notosans_thin
import com.xbot.designsystem.resources.notosans_thinitalic
import org.jetbrains.compose.resources.Font

@Composable
fun NotoSansFontFamily() = FontFamily(
    Font(Res.font.notosans_thin, FontWeight.Thin),
    Font(Res.font.notosans_thinitalic, FontWeight.Thin, FontStyle.Italic),
    Font(Res.font.notosans_extralight, FontWeight.ExtraLight),
    Font(Res.font.notosans_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(Res.font.notosans_light, FontWeight.Light),
    Font(Res.font.notosans_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(Res.font.notosans_regular, FontWeight.Normal),
    Font(Res.font.notosans_regularitalic, FontWeight.Normal, FontStyle.Italic),
    Font(Res.font.notosans_medium, FontWeight.Medium),
    Font(Res.font.notosans_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(Res.font.notosans_semibold, FontWeight.SemiBold),
    Font(Res.font.notosans_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(Res.font.notosans_bold, FontWeight.Bold),
    Font(Res.font.notosans_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(Res.font.notosans_extrabold, FontWeight.ExtraBold),
    Font(Res.font.notosans_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(Res.font.notosans_black, FontWeight.Black),
    Font(Res.font.notosans_blackitalic, FontWeight.Black, FontStyle.Italic)
)

// Default Material 3 typography values
val baseline = Typography()

@Composable
fun AnilibriaTypography() = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = NotoSansFontFamily()),
    displayMedium = baseline.displayMedium.copy(fontFamily = NotoSansFontFamily()),
    displaySmall = baseline.displaySmall.copy(fontFamily = NotoSansFontFamily()),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = NotoSansFontFamily()),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = NotoSansFontFamily()),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = NotoSansFontFamily()),
    titleLarge = baseline.titleLarge.copy(fontFamily = NotoSansFontFamily()),
    titleMedium = baseline.titleMedium.copy(fontFamily = NotoSansFontFamily()),
    titleSmall = baseline.titleSmall.copy(fontFamily = NotoSansFontFamily()),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = NotoSansFontFamily()),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = NotoSansFontFamily()),
    bodySmall = baseline.bodySmall.copy(fontFamily = NotoSansFontFamily()),
    labelLarge = baseline.labelLarge.copy(fontFamily = NotoSansFontFamily()),
    labelMedium = baseline.labelMedium.copy(fontFamily = NotoSansFontFamily()),
    labelSmall = baseline.labelSmall.copy(fontFamily = NotoSansFontFamily()),
)
