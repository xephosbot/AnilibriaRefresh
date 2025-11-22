package com.xbot.favorite.di

import org.koin.dsl.navigation3.navigation.navigation
import com.xbot.favorite.FavoriteScreen
import com.xbot.favorite.navigation.FavoriteRoute
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val favoriteFeatureModule = module {
    navigation<FavoriteRoute> {
        FavoriteScreen()
    }
}