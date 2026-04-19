package com.xbot.network.connectivity

import kotlinx.coroutines.flow.Flow

/**
 * Discrete connectivity state the app can observe or poll for pre-flight checks.
 */
enum class ConnectivityState { Available, Unavailable }

/**
 * Observes whether the device has a usable network route.
 *
 * Platform-specific implementations live in `:network:impl` and are registered as Koin
 * singletons through `@ComponentScan("com.xbot.network")`.
 *
 * **Contract:**
 * - [currentState] is a cheap synchronous snapshot used as a pre-flight gate in the
 *   HTTP layer. It must never block on I/O.
 * - [observe] emits the *current* state immediately on subscription and then every
 *   transition. Suitable for driving UI banners.
 * - On platforms where the underlying platform cannot reliably report reachability
 *   (e.g., plain JVM/desktop), the implementation defaults to
 *   [ConnectivityState.Available] — the resilience layer prefers attempting a request
 *   to failing pessimistically.
 */
interface ConnectivityMonitor {
    val currentState: ConnectivityState

    fun isOnline(): Boolean = currentState == ConnectivityState.Available

    fun observe(): Flow<ConnectivityState>
}
