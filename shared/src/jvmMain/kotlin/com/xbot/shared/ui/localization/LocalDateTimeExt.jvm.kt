package com.xbot.shared.ui.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

actual fun LocalDateTime.format(locale: Locale, format: String): String {
    return DateTimeFormatter.ofPattern(format, locale.platformLocale).format(this.toJavaLocalDateTime())
}