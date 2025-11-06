package com.xbot.common.navigation

import androidx.compose.runtime.Composable
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.KoinDslMarker
import org.koin.core.module.Module
import org.koin.core.module._singleInstanceFactory
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

@KoinExperimentalAPI
@KoinDslMarker
@OptIn(KoinInternalApi::class)
inline fun <reified T : Any> Module.navigation(
    metadata: Map<String, Any> = emptyMap(),
    noinline definition: @Composable Scope.(T) -> Unit,
): KoinDefinition<EntryProviderInstaller<T>> {
    val def = _singleInstanceFactory<EntryProviderInstaller<T>>(named<T>(), {
        val scope = this
        {
            entry<T>(
                metadata = metadata,
                content = { t -> definition(scope, t) }
            )
        }
    })
    indexPrimaryType(def)
    return KoinDefinition(this, def)
}
