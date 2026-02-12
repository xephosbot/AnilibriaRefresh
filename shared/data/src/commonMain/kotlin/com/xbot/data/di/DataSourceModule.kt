package com.xbot.data.di

import com.xbot.data.datasource.DataStoreDir
import com.xbot.data.datasource.createDataStore
import com.xbot.data.datasource.dataStoreFileName
import org.koin.dsl.module

internal val dataSourceModule = module {
    single {
        val pathProvider = get<DataStoreDir>()
        createDataStore { pathProvider.path.resolve(dataStoreFileName) }
    }
}