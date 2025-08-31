package com.xbot.sharedapp.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(appModule)
    }
}