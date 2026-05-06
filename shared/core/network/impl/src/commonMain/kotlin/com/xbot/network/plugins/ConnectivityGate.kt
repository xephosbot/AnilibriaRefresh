package com.xbot.network.plugins

import dev.jordond.connectivity.Connectivity
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

internal class NoConnectionException : IOException("No internet connection")

internal val ConnectivityGate = createClientPlugin("ConnectivityGate", ::ConnectivityGateConfig) {
    val connectivity = pluginConfig.connectivity

    on(Send) { request ->
        val isDisconnected = withContext(Dispatchers.IO) {
            connectivity?.status() == Connectivity.Status.Disconnected
        }
        if (isDisconnected) {
            throw NoConnectionException()
        }
        proceed(request)
    }
}

internal class ConnectivityGateConfig {
    var connectivity: Connectivity? = null
}
