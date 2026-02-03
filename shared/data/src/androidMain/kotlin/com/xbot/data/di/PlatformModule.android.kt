package com.xbot.data.di

import com.xbot.data.datasource.AndroidDataStorePathProvider
import com.xbot.data.datasource.DataStorePathProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val platformModule = module {
    factoryOf(::AndroidDataStorePathProvider) { bind<DataStorePathProvider>() }
}
