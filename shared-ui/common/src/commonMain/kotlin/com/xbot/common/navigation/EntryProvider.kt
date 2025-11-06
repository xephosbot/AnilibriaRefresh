package com.xbot.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import org.koin.compose.LocalKoinScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.scope.Scope

/**
 * Type alias for a function that provides navigation entries based on a route/destination.
 *
 * An [EntryProvider] takes any route object and returns the corresponding [NavEntry] that
 * contains the composable content for that destination. This enables type-safe navigation
 * with Koin dependency injection.
 *
 * @see EntryProviderInstaller for defining navigation entries in Koin modules
 */
@KoinExperimentalAPI
typealias EntryProvider<T> = (T) -> NavEntry<T>

@KoinExperimentalAPI
internal fun <T : Any> Scope.getEntryProvider() : EntryProvider<T> {
    val entries = getAll<EntryProviderInstaller<T>>()
    val entryProvider: EntryProvider<T> = entryProvider {
        entries.forEach { builder -> this.builder() }
    }
    return entryProvider
}

/**
 * Composable function that retrieves an [EntryProvider] from the current or specified Koin scope.
 *
 * This function collects all registered [EntryProviderInstaller] instances from the Koin scope
 * and aggregates them into a single [EntryProvider] that can be used with Navigation 3.
 * By default, it uses the scope from [LocalKoinScopeContext], but a custom scope can be provided.
 *
 * Example usage:
 * ```kotlin
 * @Composable
 * fun MyApp() {
 *     val entryProvider = koinEntryProvider()
 *     NavigationHost(entryProvider) {
 *         // Your navigation setup
 *     }
 * }
 * ```
 *
 * @param scope The Koin scope to retrieve navigation entries from. Defaults to [LocalKoinScopeContext.current].
 * @return An [EntryProvider] that combines all registered navigation entries from the scope
 *
 * @see EntryProvider for the navigation entry provider type
 * @see EntryProviderInstaller for defining navigation entries in Koin modules
 */
@OptIn(KoinInternalApi::class)
@KoinExperimentalAPI
@Composable
fun <T : Any> koinEntryProvider(scope : Scope = LocalKoinScope.current.getValue()) : EntryProvider<T> {
    return scope.getEntryProvider()
}