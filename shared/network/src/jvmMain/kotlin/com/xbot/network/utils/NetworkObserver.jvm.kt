package com.xbot.network.utils

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class JvmNetworkObserver(
    private val checkInterval: Duration = 3.seconds,
    private val timeout: Int = 2000,
    private val host: String = "8.8.8.8",
    private val port: Int = 53
) : NetworkObserver {

    override val isConnected: Flow<Boolean> = flow {
        emit(checkInternetConnectivity())

        while (currentCoroutineContext().isActive) {
            delay(checkInterval)
            val isConnected = checkInternetConnectivity()
            emit(isConnected)
        }
    }
        .distinctUntilChanged()

    private fun checkInternetConnectivity(): Boolean {
        val hasInterface = java.net.NetworkInterface.getNetworkInterfaces().asSequence().any { iface ->
            !iface.isLoopback && iface.isUp && !iface.isVirtual
        }

        if (!hasInterface) return false

        return try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(host, port), timeout)
                true
            }
        } catch (e: Exception) {
            false
        }
    }
}
