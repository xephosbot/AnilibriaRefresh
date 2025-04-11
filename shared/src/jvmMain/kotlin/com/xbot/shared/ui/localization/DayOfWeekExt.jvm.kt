package com.xbot.shared.ui.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek
import java.time.format.TextStyle

actual fun DayOfWeek.getName(locale: Locale): String {
    return getDisplayName(TextStyle.FULL, locale.platformLocale)
}