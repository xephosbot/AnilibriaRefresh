package com.xbot.formatters

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import com.xbot.domain.models.Episode
import com.xbot.localization.LocalAppLanguage

@Composable
fun Episode.localizedName(): String? {
    val locale = Locale(LocalAppLanguage.current)
    return localizedName(locale)
}

fun Episode.localizedName(locale: Locale = Locale.current): String? {
    return when (locale.language) {
        "ru" -> name
        else -> englishName
    }
}