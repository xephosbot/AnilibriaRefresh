package com.xbot.sharedapp.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.xbot.common.serialization.PolymorphicSerializerConfig
import com.xbot.navigation.NavKey
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.LocalKoinScopeContext
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.scope.Scope

private fun Scope.getNavSerializersModule(): SerializersModule {
    val configs = getAll<PolymorphicSerializerConfig<NavKey>>()
    val serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            configs.forEach { config -> config.configure(this) }
        }
    }
    return serializersModule
}

@OptIn(KoinInternalApi::class)
@Composable
internal fun koinNavSerializersModule(scope : Scope = LocalKoinScopeContext.current.getValue()): SerializersModule {
    return remember(scope) { scope.getNavSerializersModule() }
}
