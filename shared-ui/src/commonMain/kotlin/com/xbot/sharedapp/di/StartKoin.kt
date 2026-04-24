package com.xbot.sharedapp.di

import com.xbot.favorite.di.favoriteFeatureModule
import com.xbot.home.di.homeFeatureModule
import com.xbot.login.di.loginFeatureModule
import com.xbot.player.di.playerFeatureModule
import com.xbot.preference.di.preferenceFeatureModule
import com.xbot.search.di.searchFeatureModule
import com.xbot.title.di.titleFeatureModule
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.includes
import org.koin.dsl.koinConfiguration
import org.koin.plugin.module.dsl.koinConfiguration
import com.xbot.shared.di.initKoin as initCoreKoin

fun initKoin(
    config: KoinConfiguration? = null
) {
    initCoreKoin(
        config = koinConfiguration {
            includes(config, koinConfiguration<AnilibriaApp>())
            modules(
                favoriteFeatureModule,
                homeFeatureModule,
                playerFeatureModule,
                preferenceFeatureModule,
                searchFeatureModule,
                titleFeatureModule,
                loginFeatureModule
            )
        }
    )
}
