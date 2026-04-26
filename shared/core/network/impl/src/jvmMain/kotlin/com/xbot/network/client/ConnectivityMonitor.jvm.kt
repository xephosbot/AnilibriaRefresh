package com.xbot.network.client

import co.touchlab.kermit.Logger
import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.PollResult
import org.koin.core.annotation.Singleton

@Singleton
internal fun createConnectivityMonitor(): Connectivity = Connectivity {
    autoStart = true
    urls("https://www.cloudflare.com/")
    pollingIntervalMs = 5.minutes
    timeoutMs = 2.seconds

    onPollResult { result ->
        when (result) {
            is PollResult.Error -> Logger.e("Poll error", result.throwable)
            is PollResult.Response -> Logger.d("Poll http response: {${result.response}")
        }
    }
}
