package com.xbot.network.di

import com.xbot.network.utils.IosNetworkObserver
import com.xbot.network.utils.NetworkObserver
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual val platformNetworkModule: Module = module {
    singleOf(::IosNetworkObserver) { bind<NetworkObserver>() }
}
