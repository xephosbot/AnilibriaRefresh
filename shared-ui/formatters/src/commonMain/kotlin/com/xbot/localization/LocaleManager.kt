package com.xbot.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import org.jetbrains.compose.resources.InternalResourceApi

object LocaleManager {
    internal var isoCode by mutableStateOf<String?>(null)

    val language: AppLanguage
        @Composable get() = AppLanguage.getByIsoCode(LocalAppLanguage.current)

    fun setLocale(language: AppLanguage) {
        isoCode = language.isoCode
    }
}

/**
 * Implementation referenced from https://kotlinlang.org/docs/multiplatform/compose-resource-environment.html#locale
 */
expect object LocalAppLanguage {
    val current: String @Composable get
    @Composable infix fun provides(value: String?): ProvidedValue<*>
}

@OptIn(InternalResourceApi::class)
@Composable
fun ProvideAppLocale(content: @Composable () -> Unit) {
    val isoCode = LocaleManager.isoCode
    val language = AppLanguage.getByIsoCode(isoCode ?: LocalAppLanguage.current)
    
    CompositionLocalProvider(
        LocalAppLanguage provides isoCode,
        LocalLayoutDirection provides language.layoutDirection
    ) {
        key(isoCode) {
            content()
        }
    }
}
