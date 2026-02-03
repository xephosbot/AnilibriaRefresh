package com.xbot.data.di

import com.xbot.data.datasource.DataStorePathProvider
import com.xbot.data.datasource.createDataStore
import com.xbot.data.datasource.dataStoreFileName
import org.koin.dsl.module

val dataModule = module {
    includes(platformModule, repositoryModule)

    single {
        val pathProvider = get<DataStorePathProvider>()
        createDataStore { pathProvider.getPath(dataStoreFileName) }
    }
}
