package com.xbot.common.localization

import com.xbot.domain.models.Episode
import java.util.Locale

fun Episode.localizedName(locale: Locale = Locale.getDefault()): String? {
    return when (locale.language) {
        "ru" -> name
        else -> englishName
    }
}