package com.xbot.sharedapp.di

import com.xbot.common.serialization.PolymorphicSerializerConfig
import com.xbot.navigation.NavKey
import com.xbot.sharedapp.AppViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

internal val appModule = module {
    single<SerializersModule> {
        SerializersModule {
            polymorphic(NavKey::class) {
                getAll<PolymorphicSerializerConfig<NavKey>>().forEach { it.configure(this) }
            }
        }
    }
    viewModel<AppViewModel>()
}
