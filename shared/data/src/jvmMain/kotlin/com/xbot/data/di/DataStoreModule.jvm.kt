package com.xbot.data.di

import com.xbot.data.datasource.createDataStore
import org.koin.dsl.module

actual val dataStoreModule = module {
    single { createDataStore() }
}