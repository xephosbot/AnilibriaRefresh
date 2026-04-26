package com.xbot.network.plugins

import dev.jordond.connectivity.Connectivity
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import kotlinx.io.IOException

internal class NoConnectionException : IOException("No internet connection")

internal val ConnectivityGate = createClientPlugin("ConnectivityGate", ::ConnectivityGateConfig) {
    val connectivity = pluginConfig.connectivity

    on(Send) { request ->
        if (connectivity?.status() == Connectivity.Status.Disconnected) {
            throw NoConnectionException()
        }
        proceed(request)
    }
}

internal class ConnectivityGateConfig {
    var connectivity: Connectivity? = null
}
