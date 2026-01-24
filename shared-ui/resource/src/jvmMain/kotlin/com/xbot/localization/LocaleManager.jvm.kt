package com.xbot.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale
import java.util.prefs.Preferences

private val systemDefaultLocale by lazy { Locale.getDefault() }
private const val LOCALE_KEY = "app_locale_tag"
private val prefs = Preferences.userNodeForPackage(LocaleManager.javaClass)

actual object LocalAppLocaleIso {
    private val LocalAppLocaleIso = staticCompositionLocalOf { systemDefaultLocale.language }
    
    actual val current: String
        @Composable get() = LocalAppLocaleIso.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLocale = if (value != null) {
            prefs.put(LOCALE_KEY, value)
            Locale.forLanguageTag(value)
        } else {
            val iso = prefs.get(LOCALE_KEY, systemDefaultLocale.language)
            Locale.forLanguageTag(iso)
        }

        Locale.setDefault(newLocale)
        return LocalAppLocaleIso.provides(newLocale.language)
    }
}

fun LocaleManager.init() {
    val iso = prefs.get(LOCALE_KEY, systemDefaultLocale.language)
    val locale = Locale.forLanguageTag(iso)
    Locale.setDefault(locale)
}
