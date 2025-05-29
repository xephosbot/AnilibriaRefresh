package com.xbot.network.di

import com.xbot.network.utils.createDataStore
import com.xbot.network.utils.dataStoreFileName
import org.koin.dsl.module

actual val dataStoreModule = module {
    single { createDataStore { dataStoreFileName } }
}