package com.xbot.common.localization

import com.xbot.domain.models.Release
import java.util.Locale

fun Release.localizedName(locale: Locale = Locale.getDefault()): String {
    return when (locale.language) {
        "ru" -> name
        else -> englishName
    }
}