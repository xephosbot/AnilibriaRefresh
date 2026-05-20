package com.xbot.data.di

import java.io.File
import okio.Path.Companion.toPath
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope

@Module
actual class DataPlatformModule {

    @Singleton
    internal actual fun provideDataStoreDir(scope: Scope): DataStoreDirWrapper {
        return DataStoreDirWrapper(File(System.getProperty("java.io.tmpdir")).absolutePath.toPath())
    }
}
