package com.xbot.network.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Singleton
import java.util.concurrent.ConcurrentHashMap

/**
 * Android connectivity monitor backed by [ConnectivityManager.NetworkCallback].
 *
 * A phone typically holds more than one active network at once (Wi-Fi + cellular), and
 * "online" means *at least one* is validated. We track the set of currently validated
 * [Network] handles and derive state from `set.isNotEmpty()` — so losing Wi-Fi while
 * LTE is still up does not flip the state to offline. The set is a
 * [ConcurrentHashMap.newKeySet] so reads and writes across the system callback thread
 * and any observer thread stay race-free.
 *
 * Registered once at app startup as a Koin singleton; the callback stays alive for the
 * process lifetime. [currentState] is a `MutableStateFlow`, keeping the synchronous
 * read (used by the HTTP pre-flight check) cheap and allocation-free.
 *
 * Requires `android.permission.ACCESS_NETWORK_STATE` in the manifest.
 */
@Singleton
internal class AndroidConnectivityMonitor(
    context: Context,
) : ConnectivityMonitor {

    private val connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Thread-safe set of currently validated networks. The callback fires on a system
    // thread; state observers read from any thread. Publish to [state] after every
    // mutation so downstream sees a consistent derived value.
    private val validatedNetworks: MutableSet<Network> = ConcurrentHashMap.newKeySet()

    private val state = MutableStateFlow(readInitialState())

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            // The NetworkRequest filters by VALIDATED, but a network can lose that
            // capability mid-flight (e.g., captive portal detected) — check defensively.
            if (capabilities.isValidated()) {
                validatedNetworks.add(network)
            } else {
                validatedNetworks.remove(network)
            }
            publish()
        }

        override fun onLost(network: Network) {
            validatedNetworks.remove(network)
            publish()
        }

        override fun onUnavailable() {
            // Terminal: the callback will not fire again. Clear and publish so the
            // pre-flight check sees Unavailable rather than a stale Available.
            validatedNetworks.clear()
            publish()
        }
    }

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()
        runCatching { connectivityManager.registerNetworkCallback(request, callback) }
    }

    override val currentState: ConnectivityState
        get() = state.value

    override fun observe(): Flow<ConnectivityState> = state.asStateFlow()

    private fun publish() {
        state.value = if (validatedNetworks.isNotEmpty()) {
            ConnectivityState.Available
        } else {
            ConnectivityState.Unavailable
        }
    }

    // Seed the state before the callback fires at least once. The callback will then
    // take over and the set becomes the source of truth.
    private fun readInitialState(): ConnectivityState {
        val active = connectivityManager.activeNetwork ?: return ConnectivityState.Unavailable
        val capabilities = connectivityManager.getNetworkCapabilities(active)
            ?: return ConnectivityState.Unavailable
        return if (capabilities.isValidated()) {
            ConnectivityState.Available
        } else {
            ConnectivityState.Unavailable
        }
    }

    private fun NetworkCapabilities.isValidated(): Boolean =
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
