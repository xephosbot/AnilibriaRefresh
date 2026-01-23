package com.xbot.localization

expect object LocaleManager {
    fun setLocale(language: AppLanguage)
    fun getLocale(): AppLanguage
}