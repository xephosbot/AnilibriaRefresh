package com.xbot.api.di

import com.xbot.api.utils.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val dataStoreModule = module {
    single { createDataStore(androidContext()) }
}