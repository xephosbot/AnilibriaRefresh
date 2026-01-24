package com.xbot.localization

import androidx.compose.ui.text.intl.Locale
import com.xbot.domain.models.Franchise

fun Franchise.localizedName(locale: Locale = Locale.current): String {
    return when (locale.language) {
        "ru" -> name
        else -> englishName
    }
}