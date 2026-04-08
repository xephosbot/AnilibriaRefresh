package com.xbot.localization

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
