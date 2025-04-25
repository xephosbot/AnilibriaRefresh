package com.xbot.shared.ui.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSDateFormatter

actual fun DayOfWeek.getName(locale: Locale): String {
    val formatter = NSDateFormatter()
    formatter.locale = locale.platformLocale
    val weekdaySymbols = formatter.weekdaySymbols
    val weekday = (ordinal + 1) % 7
    return weekdaySymbols[weekday] as String
}