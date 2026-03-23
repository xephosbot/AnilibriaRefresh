package com.xbot.common.serialization

import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.KoinDslMarker
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun interface PolymorphicSerializerConfig<T : Any> {
    fun configure(builder: PolymorphicModuleBuilder<T>)
}

@OptIn(KoinInternalApi::class, ExperimentalUuidApi::class)
@KoinDslMarker
inline fun <reified T : Any> Module.polymorphic(
    crossinline builder: PolymorphicModuleBuilder<T>.() -> Unit
): KoinDefinition<PolymorphicSerializerConfig<T>> {
    return single(named(Uuid.random().toString())) {
        PolymorphicSerializerConfig { it.builder() }
    }
}
