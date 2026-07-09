package com.xbot.sharedapp.di

import dev.jordond.connectivity.Connectivity
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
expect class ConnectivityPlatformModule {
    @Singleton
    internal fun provideConnectivity(): Connectivity
}
