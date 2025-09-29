package com.xbot.sharedapp.di

import com.xbot.data.di.dataStoreModule
import com.xbot.data.di.repositoryModule
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.di.useCaseModule
import com.xbot.favorite.di.favoriteFeatureModule
import com.xbot.home.di.homeFeatureModule
import com.xbot.network.di.networkModule
import com.xbot.player.di.playerFeatureModule
import com.xbot.profile.di.profileFeatureModule
import com.xbot.search.di.searchFeatureModule
import com.xbot.title.di.titleFeatureModule
import org.koin.dsl.module

internal val appModule = module {
    includes(networkModule, dataStoreModule, repositoryModule, useCaseModule)
    includes(favoriteFeatureModule, homeFeatureModule, playerFeatureModule, profileFeatureModule, searchFeatureModule, titleFeatureModule)

    factory { SnackbarManager }
}