package com.xbot.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale
import java.util.prefs.Preferences

private val systemDefaultLocale by lazy { Locale.getDefault() }
private const val LOCALE_KEY = "app_locale_tag"
private val prefs = Preferences.userNodeForPackage(LocaleManager.javaClass)

private val storedLocale by lazy {
    prefs.get(LOCALE_KEY, null)?.let(Locale::forLanguageTag) ?: systemDefaultLocale
}

actual object LocalAppLanguage {
    private val LocalAppLocaleIso = staticCompositionLocalOf { storedLocale.language }

    actual val current: String
        @Composable get() = LocalAppLocaleIso.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLocale = if (value != null) {
            prefs.put(LOCALE_KEY, value)
            Locale.forLanguageTag(value)
        } else {
            storedLocale
        }

        Locale.setDefault(newLocale)
        return LocalAppLocaleIso.provides(newLocale.language)
    }
}

fun LocaleManager.init() {
    Locale.setDefault(storedLocale)
}
