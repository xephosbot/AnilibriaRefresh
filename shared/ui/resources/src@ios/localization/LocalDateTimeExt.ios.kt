package com.xbot.resources.localization

import platform.Foundation.NSCalendar
import platform.Foundation.NSDateFormatter
import kotlinx.datetime.LocalDateTime

actual fun LocalDateTime.format(format: String): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = format
    return dateFormatter.stringFromDate(
        toNSDate(NSCalendar.currentCalendar)
            ?: throw IllegalStateException("Could not convert kotlin date to NSDate $this")
    )
}