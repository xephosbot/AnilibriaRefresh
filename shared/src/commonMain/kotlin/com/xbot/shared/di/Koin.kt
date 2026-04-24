package com.xbot.shared.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import io.kotzilla.generated.monitoring
import org.koin.core.KoinApplication
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.includes
import org.koin.plugin.module.dsl.startKoin

fun initKoin(
    config: KoinConfiguration? = null,
) {
    startKoin<AnilibriaCore> {
        includes(config)
        kermitLogger()
        monitoring()
    }
}

fun initKoin() = initKoin(config = null)

internal fun KoinApplication.kermitLogger() {
    logger(KermitKoinLogger(Logger.withTag("koin")))
}
