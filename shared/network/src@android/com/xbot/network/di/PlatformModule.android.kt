package com.xbot.network.di

import com.xbot.network.utils.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val dataStoreModule = module {
    single { createDataStore(androidContext()) }
}