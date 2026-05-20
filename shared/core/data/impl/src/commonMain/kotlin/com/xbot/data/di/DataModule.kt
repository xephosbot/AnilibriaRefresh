package com.xbot.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module(includes = [DataPlatformModule::class])
@Configuration
@ComponentScan("com.xbot.data")
class DataModule {

    @Singleton
    internal fun createDataStore(
        cacheDir: DataStoreDirWrapper,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
        produceFile = { cacheDir.path.resolve(dataStoreFileName) }
    )
}

private const val dataStoreFileName = "app.preferences_pb"
