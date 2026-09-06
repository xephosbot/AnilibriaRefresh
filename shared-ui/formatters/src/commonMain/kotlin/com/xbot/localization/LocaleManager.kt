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

    /**
     * Records the choice with the platform. This is persistence and native-side localization only —
     * the strings Compose renders come from [ProvideAppLocale]'s resource environment.
     */
    @Composable infix fun provides(value: String?): ProvidedValue<*>
}

/**
 * Makes the in-app language the one Compose resources resolve against.
 *
 * Compose Resources does not consult the platform locale for `stringResource`: it goes through
 * `LocalComposeEnvironment`, whose default implementation reads `Locale.current`. On iOS that is
 * `NSLocale.preferredLanguages`, which Foundation fixes for the lifetime of the process — writing
 * `AppleLanguages` only takes effect on the next launch. So rather than nudging the platform locale
 * and remounting the tree to re-read it, this overrides the resource environment directly, which
 * makes a language change ordinary Compose state and applies it immediately on every platform.
 *
 * The environment is remembered per language: `LocalComposeEnvironment` is a static
 * `CompositionLocal`, so a new instance would invalidate the whole subtree on every recomposition.
 * Changing the language still recomposes everything below — but recomposition, unlike the `key()`
 * this used to rely on, keeps the state under it alive.
 *
 * Reaches for three symbols the library keeps internal: `ComposeEnvironment`,
 * `LocalComposeEnvironment` and `ResourceEnvironment`'s constructor. There is no public way to
 * supply a resource environment to `stringResource`. A Compose Resources upgrade can break this —
 * it would break at compile time, not silently.
 */
@OptIn(InternalResourceApi::class)
@Composable
fun ProvideAppLocale(content: @Composable () -> Unit) {
    val isoCode = LocaleManager.isoCode
    val language = AppLanguage.getByIsoCode(isoCode ?: LocalAppLanguage.current)
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
        // Everything but the language still comes from the library's own environment, so theme,
        // density, script and region keep behaving exactly as they would by default.
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
