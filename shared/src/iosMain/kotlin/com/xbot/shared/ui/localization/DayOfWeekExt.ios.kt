package com.xbot.shared.ui.localization

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.dateWithTimeIntervalSince1970

actual fun DayOfWeek.getName(locale: Locale): String {
    val formatter = NSDateFormatter()
    formatter.locale = NSLocale(locale.toLanguageTag())
    formatter.dateFormat = "EEEE"

    val date = NSDate.dateWithTimeIntervalSince1970(86400.0 * ordinal)
    return formatter.stringFromDate(date)
}