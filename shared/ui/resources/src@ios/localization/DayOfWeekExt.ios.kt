package com.xbot.resources.localization

import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek

actual fun DayOfWeek.getName(locale: Locale): String {
    val formatter = NSDateFormatter()
    formatter.locale = NSLocale(locale.toLanguageTag())
    formatter.dateFormat = "EEEE"

    val date = NSDate.dateWithTimeIntervalSince1970(86400.0 * ordinal).toNSDate()
    return formatter.stringFromDate(date)
}