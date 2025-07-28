package com.xbot.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek

fun DayOfWeek.toLocalizedString(locale: Locale = Locale.current): String {
    return getName(locale)
}

expect fun DayOfWeek.getName(locale: Locale): String
