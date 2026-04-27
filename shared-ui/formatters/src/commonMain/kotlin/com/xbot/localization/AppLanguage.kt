package com.xbot.localization

import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(
    val isoCode: String,
    val layoutDirection: LayoutDirection = LayoutDirection.Ltr,
) {
    English("en"),
    Russian("ru");

    val isRtl: Boolean
        get() = layoutDirection == LayoutDirection.Rtl

    companion object {
        fun getByIsoCode(isoCode: String): AppLanguage {
            return entries.find { it.isoCode.equals(isoCode, ignoreCase = true) } ?: English
        }
    }
}
