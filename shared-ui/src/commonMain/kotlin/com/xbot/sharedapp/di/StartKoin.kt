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
import io.kotzilla.generated.monitoring
import org.koin.core.KoinApplication
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.includes
import org.koin.plugin.module.dsl.startKoin

fun initKoin(
    config: KoinConfiguration? = null
) {
    startKoin<AnilibriaApp> {
        kermitLogger()
        includes(config)
        modules(
            favoriteFeatureModule,
            homeFeatureModule,
            playerFeatureModule,
            preferenceFeatureModule,
            searchFeatureModule,
            titleFeatureModule,
            loginFeatureModule
        )
        monitoring()
    }
}

internal fun KoinApplication.kermitLogger() {
    logger(KermitKoinLogger(Logger.withTag("koin")))
}
