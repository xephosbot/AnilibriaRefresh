package com.xbot.shared.ui.localization

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
actual fun LocalDateTime.format(locale: Locale, format: String): String {
    return DateTimeFormatter.ofPattern(format, locale.platformLocale).format(this.toJavaLocalDateTime())
}