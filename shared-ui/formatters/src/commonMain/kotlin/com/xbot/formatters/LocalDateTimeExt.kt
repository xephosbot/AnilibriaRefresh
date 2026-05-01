package com.xbot.formatters

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import com.xbot.localization.LocalAppLanguage
import kotlinx.datetime.LocalDateTime

@Composable
fun LocalDateTime.toLocalizedString(): String {
    val locale = Locale(LocalAppLanguage.current)
    return format(locale, "d MMM yyyy \u2022 HH:mm")
}

fun LocalDateTime.toLocalizedString(locale: Locale = Locale.current): String {
    return format(locale, "d MMM yyyy \u2022 HH:mm")
}

expect fun LocalDateTime.format(locale: Locale, format: String): String