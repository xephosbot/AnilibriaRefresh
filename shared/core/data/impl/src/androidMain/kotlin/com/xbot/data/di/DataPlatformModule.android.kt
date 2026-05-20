package com.xbot.data.di

import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope

@Module
actual class DataPlatformModule {

    @Singleton
    internal actual fun provideDataStoreDir(scope: Scope): DataStoreDirWrapper {
        return DataStoreDirWrapper(scope.androidContext().filesDir.toOkioPath())
    }
}
