package com.xbot.formatters

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import com.xbot.domain.models.Release
import com.xbot.localization.LocalAppLanguage

@Composable
fun Release.localizedName(): String {
    val locale = Locale(LocalAppLanguage.current)
    return localizedName(locale)
}

fun Release.localizedName(locale: Locale = Locale.current): String {
    return when (locale.language) {
        "ru" -> name
        else -> if (englishName != null) englishName!! else name
    }
}