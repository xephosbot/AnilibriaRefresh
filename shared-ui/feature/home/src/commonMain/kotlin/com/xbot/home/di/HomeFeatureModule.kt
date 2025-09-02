package com.xbot.home.di

import com.xbot.common.navigation.Destination
import com.xbot.home.HomeViewModel
import com.xbot.home.navigation.HomeRoute
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeFeatureModule = module {
    single<PolymorphicModuleBuilder<Destination>.() -> Unit>(named("home")) {
        {
            subclass(HomeRoute::class)
        }
    }
    viewModelOf(::HomeViewModel)
}