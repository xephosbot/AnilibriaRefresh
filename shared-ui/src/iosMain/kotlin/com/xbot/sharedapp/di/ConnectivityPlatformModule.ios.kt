package com.xbot.sharedapp.di

import dev.jordond.connectivity.Connectivity
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
actual class ConnectivityPlatformModule {

    @Singleton
    internal actual fun provideConnectivity(): Connectivity = Connectivity {
        autoStart = true
    }
}
