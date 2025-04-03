package com.xbot.api.di

import com.xbot.api.utils.createDataStore
import com.xbot.api.utils.dataStoreFileName
import org.koin.dsl.module

actual val dataStoreModule = module {
    single { createDataStore { dataStoreFileName } }
}