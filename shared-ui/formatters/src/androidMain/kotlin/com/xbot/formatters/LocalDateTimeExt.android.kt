package com.xbot.formatters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.intl.Locale
import java.time.format.DateTimeFormatter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
actual fun LocalDateTime.format(locale: Locale, format: String): String =
    DateTimeFormatter.ofPattern(format, locale.platformLocale).format(this.toJavaLocalDateTime())
