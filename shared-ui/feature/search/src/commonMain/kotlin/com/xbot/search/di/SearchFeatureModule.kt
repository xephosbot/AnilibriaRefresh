package com.xbot.search.di

import com.xbot.common.navigation.Destination
import com.xbot.search.SearchViewModel
import com.xbot.search.navigation.SearchRoute
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchFeatureModule = module {
    single<PolymorphicModuleBuilder<Destination>.() -> Unit>(named("search")) {
        {
            subclass(SearchRoute::class)
        }
    }
    viewModelOf(::SearchViewModel)
}