package com.xbot.common.localization

import java.util.Locale
import kotlin.time.Duration.Companion.seconds

fun formatDuration(seconds: Long, locale: Locale = Locale.getDefault()): String {
    val duration = seconds.seconds
    return duration.toComponents { hours, minutes, seconds, _ ->
        if (hours > 0) {
            String.format(locale, "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(locale, "%02d:%02d", minutes, seconds)
        }
    }
}