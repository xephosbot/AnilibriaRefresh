package com.xbot.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.TextStyle

actual fun DayOfWeek.getName(locale: Locale): String {
    return this.toJavaDayOfWeek().getDisplayName(TextStyle.FULL, locale.platformLocale)
}