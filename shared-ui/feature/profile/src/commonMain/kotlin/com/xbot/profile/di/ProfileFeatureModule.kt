package com.xbot.profile.di

import com.xbot.common.navigation.Destination
import com.xbot.profile.HistoryViewModel
import com.xbot.profile.ProfileViewModel
import com.xbot.profile.navigation.ProfileRoute
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileFeatureModule = module {
    single<PolymorphicModuleBuilder<Destination>.() -> Unit>(named("profile")) {
        {
            subclass(ProfileRoute::class)
        }
    }
    viewModelOf(::ProfileViewModel)
    viewModelOf(::HistoryViewModel)
}