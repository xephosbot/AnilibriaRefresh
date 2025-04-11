package com.xbot.shared.ui.localization

import androidx.compose.ui.text.intl.Locale
import kotlin.time.Duration

fun Duration.toLocalizedString(locale: Locale = Locale.current): String {
    return toComponents { hours, minutes, seconds, _ ->
        if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
    }
}