package com.xbot.data.di

import com.xbot.data.datasource.jvmCoilCacheDirProvider
import com.xbot.data.datasource.jvmDataStoreDirProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val platformModule = module {
    factoryOf(::jvmDataStoreDirProvider)
    factoryOf(::jvmCoilCacheDirProvider)
}
