package com.xbot.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDateTime

fun LocalDateTime.toLocalizedString(locale: Locale = Locale.current): String {
    return format(locale, "d MMM yyyy \u2022 HH:mm")
}

expect fun LocalDateTime.format(locale: Locale, format: String): String