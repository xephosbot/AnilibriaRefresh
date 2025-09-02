package com.xbot.title.di

import com.xbot.common.navigation.Destination
import com.xbot.title.TitleViewModel
import com.xbot.title.navigation.TitleRoute
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val titleFeatureModule = module {
    single<PolymorphicModuleBuilder<Destination>.() -> Unit>(named("title")) {
        {
            subclass(TitleRoute::class)
        }
    }
    viewModelOf(::TitleViewModel)
}