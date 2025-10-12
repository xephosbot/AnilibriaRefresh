package com.xbot.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.TextStyle

actual fun DayOfWeek.getName(locale: Locale, style: DayOfWeekStyle): String {
    val textStyle = when (style) {
        DayOfWeekStyle.FULL -> TextStyle.FULL
        DayOfWeekStyle.SHORT -> TextStyle.SHORT
    }
    return this.toJavaDayOfWeek().getDisplayName(textStyle, locale.platformLocale)
}