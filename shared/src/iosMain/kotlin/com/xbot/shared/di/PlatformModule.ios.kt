package com.xbot.shared.di

import com.xbot.shared.data.sources.local.createDataStore
import org.koin.dsl.module

actual val platformModule = module {
    single { createDataStore() }
}