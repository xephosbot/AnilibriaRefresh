package com.xbot.shared.di

import com.xbot.shared.data.sources.local.createDataStore
import com.xbot.shared.data.sources.local.dataStoreFileName
import org.koin.dsl.module

actual val platformModule = module {
    single { createDataStore { dataStoreFileName } }
}