package com.xbot.sharedapp.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.xbot.favorite.di.favoriteFeatureModule
import com.xbot.home.di.homeFeatureModule
import com.xbot.login.di.loginFeatureModule
import com.xbot.player.di.playerFeatureModule
import com.xbot.preference.di.preferenceFeatureModule
import com.xbot.search.di.searchFeatureModule
import com.xbot.title.di.titleFeatureModule
import org.koin.core.KoinApplication
import org.koin.plugin.module.dsl.startKoin

fun initKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin<AnilibriaApp> {
        kermitLogger()
        config?.invoke(this)
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
}

internal fun KoinApplication.kermitLogger() {
    logger(KermitKoinLogger(Logger.withTag("koin")))
}
