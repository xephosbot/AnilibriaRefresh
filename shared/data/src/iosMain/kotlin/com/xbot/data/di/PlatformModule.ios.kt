package com.xbot.data.di

import com.xbot.data.datasource.iosCoilCacheDirProvider
import com.xbot.data.datasource.iosDataStoreDirProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val platformModule = module {
    factoryOf(::iosDataStoreDirProvider)
    factoryOf(::iosCoilCacheDirProvider)
}
