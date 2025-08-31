package com.xbot.localization

import androidx.compose.ui.text.intl.Locale
import com.xbot.domain.models.Episode

fun Episode.localizedName(locale: Locale = Locale.current): String? {
    return when (locale.language) {
        "ru" -> name
        else -> englishName
    }
}