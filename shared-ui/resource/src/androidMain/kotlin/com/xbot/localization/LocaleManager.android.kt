package com.xbot.localization

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

actual object LocaleManager {
    actual fun setLocale(language: AppLanguage) {
        val appLocale = LocaleListCompat.forLanguageTags(language.isoCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    actual fun getLocale(): AppLanguage {
        val locales = AppCompatDelegate.getApplicationLocales()
        val tag = if (!locales.isEmpty) {
            locales.get(0)?.language
        } else {
            Locale.getDefault().language
        }
        return AppLanguage.getByIsoCode(tag ?: "en")
    }
}