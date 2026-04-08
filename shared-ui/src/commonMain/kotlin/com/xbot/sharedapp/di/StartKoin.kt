package com.xbot.sharedapp.di

import com.xbot.di.startKoin
import com.xbot.favorite.di.favoriteFeatureModule
import com.xbot.home.di.homeFeatureModule
import com.xbot.login.di.loginFeatureModule
import com.xbot.player.di.playerFeatureModule
import com.xbot.preference.di.preferenceFeatureModule
import com.xbot.search.di.searchFeatureModule
import com.xbot.title.di.titleFeatureModule
import org.koin.core.KoinApplication

fun initKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            favoriteFeatureModule,
            homeFeatureModule,
            playerFeatureModule,
            preferenceFeatureModule,
            searchFeatureModule,
            titleFeatureModule,
            loginFeatureModule
        )
    }
}
