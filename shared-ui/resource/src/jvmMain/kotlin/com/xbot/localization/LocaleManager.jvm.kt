package com.xbot.localization

import java.util.Locale
import java.util.prefs.Preferences

actual object LocaleManager {
    private const val LOCALE_KEY = "app_locale_tag"
    private val prefs = Preferences.userNodeForPackage(LocaleManager::class.java)

    actual fun setLocale(language: AppLanguage) {
        val locale = Locale.forLanguageTag(language.isoCode)
        Locale.setDefault(locale)
        prefs.put(LOCALE_KEY, language.isoCode)
    }

    actual fun getLocale(): AppLanguage {
        val tag = prefs.get(LOCALE_KEY, Locale.getDefault().toLanguageTag())
        val languageCode = Locale.forLanguageTag(tag).language
        return AppLanguage.getByIsoCode(languageCode)
    }
}

fun LocaleManager.applyLocale() {
    val language = getLocale()
    Locale.setDefault(Locale.forLanguageTag(language.isoCode))
}