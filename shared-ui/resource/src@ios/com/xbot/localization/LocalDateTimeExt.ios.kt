package com.xbot.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter

actual fun LocalDateTime.format(locale: Locale, format: String): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = format
    dateFormatter.locale = locale.platformLocale
    return dateFormatter.stringFromDate(toInstant(TimeZone.currentSystemDefault()).toNSDate())
}