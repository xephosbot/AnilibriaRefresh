package com.xbot.network.di

import com.xbot.network.utils.JvmNetworkObserver
import com.xbot.network.utils.NetworkObserver
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformNetworkModule: Module = module {
    single<NetworkObserver> { JvmNetworkObserver() }
}
