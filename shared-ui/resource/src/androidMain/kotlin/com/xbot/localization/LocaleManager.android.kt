package com.xbot.localization

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.os.LocaleListCompat
import java.util.Locale

actual object LocalAppLocaleIso {
    actual val current: String
        @Composable get() {
            val locales = AppCompatDelegate.getApplicationLocales()
            return if (!locales.isEmpty) {
                locales[0]?.language ?: Locale.getDefault().language
            } else {
                Locale.getDefault().language
            }
        }

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val configuration = LocalConfiguration.current
        if (value != null) {
            val appLocale = LocaleListCompat.forLanguageTags(value)
            AppCompatDelegate.setApplicationLocales(appLocale)

            configuration.setLocale(LocaleListCompat.getAdjustedDefault()[0])
            configuration.setLayoutDirection(LocaleListCompat.getAdjustedDefault()[0])
        }

        return LocalConfiguration.provides(configuration)
    }
}
