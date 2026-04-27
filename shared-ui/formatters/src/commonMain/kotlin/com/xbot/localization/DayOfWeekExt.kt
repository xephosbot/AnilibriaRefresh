package com.xbot.localization

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek

@Composable
fun DayOfWeek.toLocalizedString(
    style: DayOfWeekStyle = DayOfWeekStyle.FULL
): String {
    val locale = Locale(LocalAppLanguage.current)
    return getName(locale, style)
}

fun DayOfWeek.toLocalizedString(
    locale: Locale = Locale.current,
    style: DayOfWeekStyle = DayOfWeekStyle.FULL
): String {
    return getName(locale, style)
}

expect fun DayOfWeek.getName(locale: Locale, style: DayOfWeekStyle): String

enum class DayOfWeekStyle {
    FULL,
    SHORT
}
