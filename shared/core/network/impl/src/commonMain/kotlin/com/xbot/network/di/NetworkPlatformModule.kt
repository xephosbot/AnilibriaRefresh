package com.xbot.network.di

import dev.jordond.connectivity.Connectivity
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
expect class NetworkPlatformModule {

    @Singleton
    internal fun provideConnectivity(): Connectivity
}
