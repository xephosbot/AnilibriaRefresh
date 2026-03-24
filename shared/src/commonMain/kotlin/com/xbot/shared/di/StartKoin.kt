package com.xbot.shared.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import org.koin.core.KoinApplication
import org.koin.plugin.module.dsl.startKoin

fun initKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin<AnilibriaApp> {
        kermitLogger()
        config?.invoke(this)
    }
}

internal fun KoinApplication.kermitLogger() {
    logger(KermitKoinLogger(Logger.withTag("koin")))
}
