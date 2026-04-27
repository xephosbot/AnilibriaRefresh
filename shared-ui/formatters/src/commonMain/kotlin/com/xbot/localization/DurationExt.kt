package com.xbot.localization

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import kotlin.time.Duration

@Composable
fun Duration.toLocalizedString(): String {
    val locale = Locale(LocalAppLanguage.current)
    return toLocalizedString(locale)
}

fun Duration.toLocalizedString(locale: Locale = Locale.current): String {
    return toComponents { hours, minutes, seconds, _ ->
        if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
    }
}