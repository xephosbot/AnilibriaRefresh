package com.xbot.common.localization

import kotlinx.datetime.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

fun DayOfWeek.toLocalizedString(locale: Locale = Locale.getDefault()): String {
    return getDisplayName(TextStyle.FULL, locale).run {
        replaceFirstChar { it.uppercase() }
    }
}