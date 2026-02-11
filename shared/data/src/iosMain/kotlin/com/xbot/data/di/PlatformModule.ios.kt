package com.xbot.data.di

import com.xbot.data.datasource.DataStorePathProvider
import com.xbot.data.datasource.IosDataStorePathProvider
import com.xbot.data.datasource.IosCoilCacheDirProvider
import com.xbot.network.utils.CoilCacheDirProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val platformModule = module {
    factoryOf(::IosDataStorePathProvider) { bind<DataStorePathProvider>() }
    factoryOf(::IosCoilCacheDirProvider) { bind<CoilCacheDirProvider>() }
}
