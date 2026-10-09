package com.xbot.formatters

import androidx.compose.ui.text.intl.Locale
import kotlin.time.Duration
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierGregorian
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSDateComponentsFormatter
import platform.Foundation.NSDateComponentsFormatterUnitsStyleShort
import platform.Foundation.NSDateComponentsFormatterZeroFormattingBehaviorDropAll
import platform.Foundation.NSLocale

actual fun Duration.toLocalizedUnits(locale: Locale): String {
    val formatter = NSDateComponentsFormatter()
    formatter.calendar = NSCalendar.calendarWithIdentifier(NSCalendarIdentifierGregorian)
        ?.also { it.locale = NSLocale(locale.toLanguageTag()) }
    formatter.allowedUnits = NSCalendarUnitHour or NSCalendarUnitMinute
    formatter.unitsStyle = NSDateComponentsFormatterUnitsStyleShort
    formatter.zeroFormattingBehavior = NSDateComponentsFormatterZeroFormattingBehaviorDropAll
    return formatter.stringFromTimeInterval(inWholeSeconds.toDouble()) ?: toString()
}
