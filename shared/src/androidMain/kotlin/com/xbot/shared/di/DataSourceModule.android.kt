package com.xbot.shared.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.xbot.shared.data.sources.local.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val localDataSourceModule = module {
    single<DataStore<Preferences>> { createDataStore(androidContext()) }
}