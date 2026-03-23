package com.xbot.favorite.di

import com.xbot.common.navigation.NavKey
import com.xbot.common.serialization.polymorphic
import com.xbot.favorite.FavoriteScreen
import com.xbot.favorite.navigation.FavoriteRoute
import kotlinx.serialization.modules.subclass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val favoriteFeatureModule = module {
    polymorphic<NavKey> {
        subclass(FavoriteRoute::class)
    }
    navigation<FavoriteRoute> {
        FavoriteScreen()
    }
}
