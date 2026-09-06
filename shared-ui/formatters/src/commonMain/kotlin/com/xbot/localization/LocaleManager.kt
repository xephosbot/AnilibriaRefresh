@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.xbot.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import org.jetbrains.compose.resources.ComposeEnvironment
import org.jetbrains.compose.resources.DefaultComposeEnvironment
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.LocalComposeEnvironment
import org.jetbrains.compose.resources.ResourceEnvironment

object LocaleManager {
    internal var isoCode by mutableStateOf<String?>(null)

    val language: AppLanguage
        @Composable get() = AppLanguage.getByIsoCode(isoCode ?: LocalAppLanguage.current)

    fun setLocale(language: AppLanguage) {
        isoCode = language.isoCode
    }
}

expect object LocalAppLanguage {
    val current: String @Composable get

    /** Platform persistence and native-side localization only; Compose strings come from [ProvideAppLocale]. */
    @Composable infix fun provides(value: String?): ProvidedValue<*>
}

/**
 * Overrides the Compose Resources environment so a language change applies immediately. Nudging the
 * platform locale does not work on iOS: Foundation fixes `preferredLanguages` for the process
 * lifetime. Uses internal Compose Resources symbols — an upgrade breaks this at compile time.
 */
@OptIn(InternalResourceApi::class)
@Composable
fun ProvideAppLocale(content: @Composable () -> Unit) {
    val isoCode = LocaleManager.isoCode
    val language = AppLanguage.getByIsoCode(isoCode ?: LocalAppLanguage.current)
    // Remembered per language: LocalComposeEnvironment is static, so a fresh instance every
    // recomposition would invalidate the whole subtree.
    val environment = remember(language) { AppComposeEnvironment(language.isoCode) }

    CompositionLocalProvider(
        LocalAppLanguage provides isoCode,
        LocalLayoutDirection provides language.layoutDirection,
        LocalComposeEnvironment provides environment,
    ) {
        content()
    }
}

@OptIn(InternalResourceApi::class)
private class AppComposeEnvironment(private val languageCode: String) : ComposeEnvironment {

    @Composable
    override fun rememberEnvironment(): ResourceEnvironment {
        // Only the language is overridden; theme, density, script and region keep their defaults.
        val default = DefaultComposeEnvironment.rememberEnvironment()
        return remember(languageCode, default) {
            ResourceEnvironment(
                language = LanguageQualifier(languageCode),
                script = default.script,
                region = default.region,
                theme = default.theme,
                density = default.density,
            )
        }
    }
}
