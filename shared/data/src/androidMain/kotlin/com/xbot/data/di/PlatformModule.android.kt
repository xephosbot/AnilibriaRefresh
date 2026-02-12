package com.xbot.data.di

import com.xbot.data.datasource.androidCoilCacheDirProvider
import com.xbot.data.datasource.androidDataStoreDirProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val platformModule = module {
    factoryOf(::androidDataStoreDirProvider)
    factoryOf(::androidCoilCacheDirProvider)
}
