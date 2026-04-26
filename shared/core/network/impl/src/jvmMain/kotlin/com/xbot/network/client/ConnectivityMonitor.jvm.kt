package com.xbot.network.client

import dev.jordond.connectivity.Connectivity
import org.koin.core.annotation.Singleton

@Singleton
internal fun createConnectivityMonitor(): Connectivity = Connectivity {
    autoStart = true
    urls("https://www.cloudflare.com/")
    pollingIntervalMs = 5.minutes
    timeoutMs = 2.seconds
}
