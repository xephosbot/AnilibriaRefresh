package com.xbot.data.di

import com.xbot.data.datasource.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal actual val platformModule = module {
    single { createDataStore(androidContext()) }
}