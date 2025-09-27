package com.xbot.localization

import androidx.compose.ui.text.intl.Locale
import com.xbot.domain.models.Release

fun Release.localizedName(locale: Locale = Locale.current): String {
    return when (locale.language) {
        "ru" -> name
        else -> if (englishName != null) englishName!! else name
    }
}