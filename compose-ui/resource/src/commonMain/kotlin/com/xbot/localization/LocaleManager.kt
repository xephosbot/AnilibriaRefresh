package com.xbot.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object LocaleManager {
    internal var isoCode by mutableStateOf<String?>(null)

    val locale: AppLanguage
        @Composable get() = AppLanguage.getByIsoCode(LocalAppLocaleIso.current)

    fun setLocale(language: AppLanguage) {
        isoCode = language.isoCode
    }
}

/**
 * Implementation referenced from https://kotlinlang.org/docs/multiplatform/compose-resource-environment.html#locale
 */
expect object LocalAppLocaleIso {
    val current: String @Composable get
    @Composable infix fun provides(value: String?): ProvidedValue<*>
}

@Composable
fun ProvideAppLocale(content: @Composable () -> Unit) {
    val locale = LocaleManager.isoCode
    CompositionLocalProvider(
        LocalAppLocaleIso provides locale,
    ) {
        key(locale) {
            content()
        }
    }
}
