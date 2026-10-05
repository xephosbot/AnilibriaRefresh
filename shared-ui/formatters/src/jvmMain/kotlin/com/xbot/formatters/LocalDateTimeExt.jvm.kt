package com.xbot.formatters

import androidx.compose.ui.text.intl.Locale
import java.time.format.DateTimeFormatter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime

actual fun LocalDateTime.format(locale: Locale, format: String): String =
    DateTimeFormatter.ofPattern(format, locale.platformLocale).format(this.toJavaLocalDateTime())
