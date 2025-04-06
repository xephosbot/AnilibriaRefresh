package com.xbot.resources.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDateTime

fun LocalDateTime.toLocalizedString(locale: Locale = Locale.current): String {
    return format("d MMM. yyyy HH:mm")
}

expect fun LocalDateTime.format(format: String): String