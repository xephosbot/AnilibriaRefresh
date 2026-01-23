package com.xbot.localization

import com.xbot.resources.Res
import com.xbot.resources.preference_language_en
import com.xbot.resources.preference_language_ru
import org.jetbrains.compose.resources.StringResource

enum class AppLanguage(
    val isoCode: String
) {
    English("en"),
    Russian("ru");

    companion object {
        fun getByIsoCode(isoCode: String): AppLanguage {
            return entries.find { it.isoCode.equals(isoCode, ignoreCase = true) } ?: English
        }
    }
}

val AppLanguage.title: StringResource
    get() = when (this) {
        AppLanguage.English -> Res.string.preference_language_en
        AppLanguage.Russian -> Res.string.preference_language_ru
    }
