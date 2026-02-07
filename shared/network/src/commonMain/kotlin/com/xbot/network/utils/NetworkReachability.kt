package com.xbot.network.utils

import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.content.OutgoingContent
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.launch
import kotlinx.io.IOException

class NetworkReachabilityConfig {
    var networkObserver: NetworkObserver? = null
}

val NetworkReachability = createClientPlugin("NetworkReachability", ::NetworkReachabilityConfig) {
    val networkObserver = pluginConfig.networkObserver

    val isNetworkAvailable = atomic(true)

    val observationJob = networkObserver?.let { observer ->
        client.launch {
            observer.isConnected.collect { connected ->
                isNetworkAvailable.value = connected
            }
        }
    }

    onClose {
        observationJob?.cancel()
    }

    on(Send) { request ->
        if (!isNetworkAvailable.value) {
            networkObserver?.awaitConnection()
        }

        val content = request.body as? OutgoingContent
        val isRetryableBody = content == null ||
                content is OutgoingContent.NoContent ||
                content is OutgoingContent.ByteArrayContent ||
                content is OutgoingContent.ProtocolUpgrade

        try {
            proceed(request)
        } catch (cause: Throwable) {
            if (isConnectivityError(cause) && isRetryableBody) {
                networkObserver?.awaitConnection()
                proceed(request)
            } else {
                throw cause
            }
        }
    }
}

private fun isConnectivityError(cause: Throwable): Boolean {
    return cause is IOException
            || cause::class.simpleName?.contains("SocketException") == true
            || cause::class.simpleName?.contains("UnknownHostException") == true
            || (cause.message ?: "").contains("Network is unreachable")
}
