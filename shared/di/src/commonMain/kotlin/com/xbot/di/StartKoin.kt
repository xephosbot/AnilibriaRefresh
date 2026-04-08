package com.xbot.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import org.koin.core.KoinApplication
import org.koin.plugin.module.dsl.startKoin

fun startKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin<AnilibriaApp> {
        kermitLogger()
        config?.invoke(this)
        modules(viewModelModule)
    }
}

internal fun KoinApplication.kermitLogger() {
    logger(KermitKoinLogger(Logger.withTag("koin")))
}
