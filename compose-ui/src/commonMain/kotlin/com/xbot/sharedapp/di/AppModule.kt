@file:OptIn(ExperimentalObjCRefinement::class)

package com.xbot.sharedapp.di

import com.xbot.common.navigation.NavKey
import com.xbot.common.serialization.PolymorphicSerializerConfig
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.sharedapp.AppViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import kotlin.experimental.ExperimentalObjCRefinement

internal val appModule = module {
    single { SnackbarManager }
    single {
        val configs: List<PolymorphicSerializerConfig<NavKey>> = getAll()
        SerializersModule {
            polymorphic(NavKey::class) {
                configs.forEach { it.configure(this) }
            }
        }
    }
    viewModelOf(::AppViewModel)
}
