package com.xbot.data.di

import okio.Path
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope
import kotlin.jvm.JvmInline

@Module
expect class DataPlatformModule {

    @Singleton
    internal fun provideDataStoreDir(scope: Scope): DataStoreDirWrapper
}

@JvmInline
internal value class DataStoreDirWrapper(val path: Path)
