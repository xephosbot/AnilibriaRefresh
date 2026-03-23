package com.xbot.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSDateFormatter

actual fun DayOfWeek.getName(locale: Locale, style: DayOfWeekStyle): String {
    val formatter = NSDateFormatter()
    formatter.locale = locale.platformLocale
    val weekdaySymbols = when (style) {
        DayOfWeekStyle.FULL -> formatter.weekdaySymbols
        DayOfWeekStyle.SHORT -> formatter.shortWeekdaySymbols
    }
    val weekday = (ordinal + 1) % 7
    return weekdaySymbols[weekday] as String
}