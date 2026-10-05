package com.xbot.formatters

import androidx.compose.ui.text.intl.Locale
import com.xbot.domain.models.Franchise

fun Franchise.localizedName(locale: Locale = Locale.current): String = when (locale.language) {
    "ru" -> name
    else -> englishName
}
