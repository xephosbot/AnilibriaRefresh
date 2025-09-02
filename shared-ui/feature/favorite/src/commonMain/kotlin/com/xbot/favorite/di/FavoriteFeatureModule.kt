package com.xbot.favorite.di

import com.xbot.common.navigation.Destination
import com.xbot.favorite.navigation.FavoriteRoute
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass
import org.koin.core.qualifier.named
import org.koin.dsl.module

val favoriteFeatureModule = module {
    single<PolymorphicModuleBuilder<Destination>.() -> Unit>(named("favorite")) {
        {
            subclass(FavoriteRoute::class)
        }
    }
}