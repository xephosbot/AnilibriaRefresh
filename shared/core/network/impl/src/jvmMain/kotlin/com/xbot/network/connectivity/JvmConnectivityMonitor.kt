package com.xbot.network.connectivity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Singleton

/**
 * JVM/desktop connectivity stub.
 *
 * Plain JVM environments are too varied for a reliable cheap reachability check
 * (corporate proxies, VPNs, captive portals, chroot jails). The stub reports
 * [ConnectivityState.Available] unconditionally — the resilience layer will surface
 * real failures via `ConnectionError` / `Timeout` and their retry policies rather than
 * gate on a pessimistic local guess.
 */
@Singleton
internal class JvmConnectivityMonitor : ConnectivityMonitor {
    private val state = MutableStateFlow(ConnectivityState.Available)

    override val currentState: ConnectivityState
        get() = state.value

    override fun observe(): Flow<ConnectivityState> = state.asStateFlow()
}
