package com.xbot.favorite.di

import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.favorite.FavoriteScreen
import com.xbot.favorite.navigation.FavoriteRoute
import org.koin.core.qualifier.named
import org.koin.dsl.module

val favoriteFeatureModule = module {
    single<NavEntryBuilder>(named("feature/favorite")) {
        {
            entry<FavoriteRoute> {
                FavoriteScreen()
            }
        }
    }
}