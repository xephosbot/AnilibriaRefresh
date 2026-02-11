package com.xbot.data.di

import com.xbot.data.datasource.DataStorePathProvider
import com.xbot.data.datasource.JvmDataStorePathProvider
import com.xbot.data.datasource.JvmCoilCacheDirProvider
import com.xbot.network.utils.CoilCacheDirProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val platformModule = module {
    factoryOf(::JvmDataStorePathProvider) { bind<DataStorePathProvider>() }
    factoryOf(::JvmCoilCacheDirProvider) { bind<CoilCacheDirProvider>() }
}
